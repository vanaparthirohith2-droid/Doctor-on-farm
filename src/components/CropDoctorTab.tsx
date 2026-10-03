import React, { useState, useRef, useEffect } from 'react';
import {
  Camera,
  Upload,
  Volume2,
  VolumeX,
  Bookmark,
  Sparkles,
  CheckCircle,
  AlertTriangle,
  Leaf,
  Pill,
  Clock,
  X,
  Search,
  ArrowRight
} from 'lucide-react';
import { AppLanguage, DiseaseDiagnosis } from '../types';
import { SAMPLE_LEAVES, SampleCropLeaf, getOfflineDiagnosis } from '../data/mockData';
import { analyzeCropWithGemini, searchCropIssueWithGemini } from '../services/geminiService';
import { CameraCaptureModal } from './CameraCaptureModal';
import { t } from '../utils/translations';

interface CropDoctorTabProps {
  currentLanguage: AppLanguage;
  onSaveToHistory: (diag: DiseaseDiagnosis) => void;
}

export const CropDoctorTab: React.FC<CropDoctorTabProps> = ({
  currentLanguage,
  onSaveToHistory
}) => {
  const [showCamera, setShowCamera] = useState(false);
  const [selectedImage, setSelectedImage] = useState<string | null>(null);
  const [selectedSample, setSelectedSample] = useState<SampleCropLeaf | null>(null);
  const [isAnalyzing, setIsAnalyzing] = useState(false);
  const [diagnosis, setDiagnosis] = useState<DiseaseDiagnosis | null>(null);
  const [isSpeaking, setIsSpeaking] = useState(false);
  const [treatmentTab, setTreatmentTab] = useState<'organic' | 'chemical'>('organic');
  const [savedSuccess, setSavedSuccess] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [isSearchingAi, setIsSearchingAi] = useState(false);

  const fileInputRef = useRef<HTMLInputElement | null>(null);

  // If language changes and user has a sample selected, update the diagnosis language
  useEffect(() => {
    if (selectedSample) {
      setDiagnosis(getOfflineDiagnosis(selectedSample.id, currentLanguage.code));
    }
  }, [currentLanguage]);

  const handleCapture = async (base64Image: string) => {
    setSelectedImage(base64Image);
    setSelectedSample(null);
    setDiagnosis(null);
    setIsAnalyzing(true);

    try {
      const result = await analyzeCropWithGemini(base64Image, currentLanguage);
      setDiagnosis(result);
    } catch {
      setDiagnosis(getOfflineDiagnosis('tomato_early_blight', currentLanguage.code));
    } finally {
      setIsAnalyzing(false);
    }
  };

  const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = async () => {
      const base64 = reader.result as string;
      setSelectedImage(base64);
      setSelectedSample(null);
      setDiagnosis(null);
      setIsAnalyzing(true);

      try {
        const result = await analyzeCropWithGemini(base64, currentLanguage);
        setDiagnosis(result);
      } catch {
        setDiagnosis(getOfflineDiagnosis('tomato_early_blight', currentLanguage.code));
      } finally {
        setIsAnalyzing(false);
      }
    };
    reader.readAsDataURL(file);
  };

  const handleSampleClick = (sample: SampleCropLeaf) => {
    setSelectedSample(sample);
    setSelectedImage(null);
    setIsAnalyzing(true);

    setTimeout(() => {
      const diag = getOfflineDiagnosis(sample.id, currentLanguage.code);
      setDiagnosis(diag);
      setIsAnalyzing(false);
    }, 250);
  };

  const handleAiSearch = async (e?: React.FormEvent) => {
    if (e) e.preventDefault();
    if (!searchQuery.trim()) return;

    setIsSearchingAi(true);
    setSelectedImage(null);
    setSelectedSample(null);
    setDiagnosis(null);

    try {
      const result = await searchCropIssueWithGemini(searchQuery.trim(), currentLanguage);
      setDiagnosis(result);
    } catch {
      setDiagnosis(getOfflineDiagnosis('wheat_yellow_rust', currentLanguage.code));
    } finally {
      setIsSearchingAi(false);
    }
  };

  const toggleSpeech = () => {
    if (!('speechSynthesis' in window)) return;

    if (isSpeaking) {
      window.speechSynthesis.cancel();
      setIsSpeaking(false);
      return;
    }

    if (!diagnosis) return;

    window.speechSynthesis.cancel();
    const narrationText = diagnosis.isHealthy
      ? `${diagnosis.cropName}। ${t('healthyBadge', currentLanguage.code)}। ${diagnosis.summary}`
      : `${diagnosis.cropName}। ${diagnosis.diseaseName}। ${diagnosis.summary}। ${t('tabOrganic', currentLanguage.code)}: ${diagnosis.organicTreatments.join(', ')}।`;

    const utterance = new SpeechSynthesisUtterance(narrationText);
    utterance.lang = currentLanguage.ttsVoiceLang;
    utterance.rate = 0.9;

    utterance.onend = () => setIsSpeaking(false);
    utterance.onerror = () => setIsSpeaking(false);

    setIsSpeaking(true);
    window.speechSynthesis.speak(utterance);
  };

  const handleSave = () => {
    if (!diagnosis) return;
    onSaveToHistory(diagnosis);
    setSavedSuccess(true);
    setTimeout(() => setSavedSuccess(false), 2000);
  };

  const popularQueries = [
    { label: currentLanguage.code === 'hi' ? 'गेहूं में पीला रतुआ' : 'Wheat Yellow Rust', q: 'Wheat yellow stripe rust treatment' },
    { label: currentLanguage.code === 'hi' ? 'टमाटर अगेती झुलसा' : 'Tomato Early Blight', q: 'Tomato early blight organic spray' },
    { label: currentLanguage.code === 'hi' ? 'धान में ब्लास्ट रोग' : 'Rice Blast Disease', q: 'Rice blast fungus control fungicide' },
    { label: currentLanguage.code === 'hi' ? 'कपास गुलाबी सुंडी' : 'Cotton Bollworm', q: 'Cotton pink bollworm chemical control' }
  ];

  return (
    <div className="pb-28">
      {/* Hidden file input for gallery picker */}
      <input
        ref={fileInputRef}
        type="file"
        accept="image/*"
        onChange={handleFileUpload}
        className="hidden"
      />

      {/* Hero Welcome banner with Logo & Highlighted Varma Badge */}
      <div className="bg-gradient-to-r from-emerald-950 via-emerald-900 to-emerald-800 text-white px-5 py-6 rounded-b-3xl shadow-sm">
        <div className="flex items-center gap-3.5">
          <img
            src="/logo.jpg"
            alt="Doctor on Farm Logo"
            className="w-14 h-14 rounded-2xl object-cover ring-2 ring-amber-400 shadow-lg shrink-0"
          />
          <div>
            <div className="flex items-center gap-2 flex-wrap">
              <h1 className="text-xl font-extrabold tracking-tight">Doctor on Farm</h1>
              <span className="text-[10px] px-2 py-0.5 rounded-full bg-emerald-700 text-emerald-200 font-bold border border-emerald-500/50">
                PRO AI
              </span>
              <span className="px-2.5 py-0.5 rounded-full bg-gradient-to-r from-amber-400 to-yellow-300 text-neutral-950 text-xs font-black shadow-md ring-2 ring-amber-300/80 animate-pulse">
                ⭐ ROHITH VARMA
              </span>
            </div>
            <p className="text-xs text-amber-300 font-bold mt-1 flex items-center gap-1.5">
              <span>🌾 Lead Plant Doctor: <span className="underline decoration-amber-400 decoration-2 underline-offset-2 font-black text-amber-200">Rohith Varma</span> (रोहित वर्मा)</span>
            </p>
            <p className="text-[11px] text-emerald-100/90 font-medium mt-0.5">
              {t('heroSubtitle', currentLanguage.code)}
            </p>
          </div>
        </div>
      </div>

      <div className="px-4 mt-5 space-y-5">
        {/* Gemini AI Pathology Search Bar */}
        <div className="p-3.5 rounded-2xl bg-white dark:bg-neutral-800 border border-emerald-200/80 dark:border-neutral-700 shadow-sm">
          <form onSubmit={handleAiSearch} className="flex items-center gap-2">
            <div className="relative flex-1">
              <Search className="w-4 h-4 text-emerald-600 absolute left-3 top-3" />
              <input
                type="text"
                value={searchQuery}
                onChange={e => setSearchQuery(e.target.value)}
                placeholder={t('aiSearchPlaceholder', currentLanguage.code)}
                className="w-full pl-9 pr-3 py-2 text-xs rounded-xl bg-neutral-50 dark:bg-neutral-900 border border-neutral-200 dark:border-neutral-700 focus:outline-none focus:border-emerald-500 text-neutral-800 dark:text-neutral-200"
              />
            </div>
            <button
              type="submit"
              disabled={isSearchingAi || !searchQuery.trim()}
              className="py-2 px-3.5 bg-emerald-600 hover:bg-emerald-500 disabled:opacity-50 text-white text-xs font-bold rounded-xl transition flex items-center gap-1.5 shrink-0 active:scale-95"
            >
              <Sparkles className="w-3.5 h-3.5" />
              <span>{t('aiSearchBtn', currentLanguage.code)}</span>
            </button>
          </form>

          {/* Quick Search Chips */}
          <div className="flex gap-1.5 overflow-x-auto mt-2.5 pt-1 scrollbar-none">
            {popularQueries.map((item, idx) => (
              <button
                key={idx}
                onClick={() => {
                  setSearchQuery(item.q);
                  setIsSearchingAi(true);
                  searchCropIssueWithGemini(item.q, currentLanguage).then(res => {
                    setDiagnosis(res);
                    setIsSearchingAi(false);
                  });
                }}
                className="shrink-0 px-2.5 py-1 bg-emerald-50 dark:bg-emerald-950/40 text-emerald-800 dark:text-emerald-300 rounded-lg text-[10px] font-semibold border border-emerald-200/60 dark:border-emerald-900/50 hover:bg-emerald-100 transition"
              >
                🔍 {item.label}
              </button>
            ))}
          </div>
        </div>

        {/* Action Buttons: Camera & Gallery */}
        <div>
          <div className="grid grid-cols-2 gap-3">
            {/* Live Camera Button */}
            <button
              onClick={() => setShowCamera(true)}
              className="p-4 rounded-2xl bg-emerald-50 dark:bg-emerald-950/40 border-2 border-emerald-500/80 hover:bg-emerald-100/70 transition flex flex-col items-center justify-center text-center group active:scale-98 shadow-sm"
            >
              <div className="w-12 h-12 rounded-full bg-emerald-600 text-white flex items-center justify-center mb-2 shadow group-hover:scale-105 transition">
                <Camera className="w-6 h-6" />
              </div>
              <span className="font-bold text-sm text-emerald-950 dark:text-emerald-200">
                {t('cameraScanBtn', currentLanguage.code)}
              </span>
              <span className="text-[11px] text-emerald-700 dark:text-emerald-400 font-medium">
                {t('cameraScanDesc', currentLanguage.code)}
              </span>
            </button>

            {/* Gallery Upload Button */}
            <button
              onClick={() => fileInputRef.current?.click()}
              className="p-4 rounded-2xl bg-white dark:bg-neutral-800 border border-neutral-200 dark:border-neutral-700 hover:border-emerald-300 transition flex flex-col items-center justify-center text-center group active:scale-98 shadow-sm"
            >
              <div className="w-12 h-12 rounded-full bg-neutral-100 dark:bg-neutral-700 text-emerald-600 dark:text-emerald-400 flex items-center justify-center mb-2 shadow-sm group-hover:scale-105 transition">
                <Upload className="w-6 h-6" />
              </div>
              <span className="font-bold text-sm text-neutral-800 dark:text-neutral-200">
                {t('uploadPhotoBtn', currentLanguage.code)}
              </span>
              <span className="text-[11px] text-neutral-500 font-medium">
                {t('uploadPhotoDesc', currentLanguage.code)}
              </span>
            </button>
          </div>
        </div>

        {/* Quick Test Sample Leaves */}
        <div>
          <div className="flex items-center justify-between mb-2">
            <h2 className="text-sm font-bold text-neutral-800 dark:text-neutral-200">
              {t('quickTestTitle', currentLanguage.code)}
            </h2>
            <span className="text-xs text-emerald-600 font-semibold">
              {t('quickTestBadge', currentLanguage.code)}
            </span>
          </div>

          <div className="flex gap-2.5 overflow-x-auto pb-2 scrollbar-none">
            {SAMPLE_LEAVES.map(sample => {
              const isSelected = selectedSample?.id === sample.id;
              return (
                <button
                  key={sample.id}
                  onClick={() => handleSampleClick(sample)}
                  className={`shrink-0 w-36 p-3 rounded-2xl border text-left transition ${
                    isSelected
                      ? 'border-emerald-600 bg-emerald-50 dark:bg-emerald-950/40 ring-2 ring-emerald-500/20'
                      : 'border-neutral-200 dark:border-neutral-800 bg-white dark:bg-neutral-800/80 hover:border-neutral-300'
                  }`}
                >
                  <div
                    className="w-10 h-10 rounded-xl flex items-center justify-center text-xl mb-2"
                    style={{ backgroundColor: sample.bgColor }}
                  >
                    {sample.icon}
                  </div>
                  <div className="font-bold text-xs text-neutral-900 dark:text-white truncate">
                    {currentLanguage.code === 'hi' ? sample.nameHi : sample.nameEn}
                  </div>
                  <div className="text-[10px] text-neutral-500 truncate mt-0.5">
                    {currentLanguage.code === 'hi' ? sample.diseaseHi : sample.diseaseEn}
                  </div>
                </button>
              );
            })}
          </div>
        </div>

        {/* Selected Image Preview */}
        {selectedImage && (
          <div className="relative rounded-2xl overflow-hidden border border-neutral-200 dark:border-neutral-700 shadow-sm max-h-56 bg-black">
            <img src={selectedImage} alt="Crop Leaf" className="w-full h-56 object-cover" />
            <button
              onClick={() => {
                setSelectedImage(null);
                setDiagnosis(null);
              }}
              className="absolute top-3 right-3 p-1.5 rounded-full bg-black/60 text-white hover:bg-black/80 transition"
            >
              <X className="w-4 h-4" />
            </button>
          </div>
        )}

        {/* Loading / Searching Animation */}
        {(isAnalyzing || isSearchingAi) && (
          <div className="p-5 rounded-2xl bg-emerald-50 dark:bg-emerald-950/40 border border-emerald-200 dark:border-emerald-900 flex items-center gap-3">
            <div className="w-8 h-8 border-3 border-emerald-600 border-t-transparent rounded-full animate-spin shrink-0"></div>
            <div>
              <p className="font-bold text-sm text-emerald-900 dark:text-emerald-200">
                {isSearchingAi
                  ? t('aiSearching', currentLanguage.code)
                  : t('diagnosing', currentLanguage.code)}
              </p>
              <p className="text-xs text-emerald-700 dark:text-emerald-400 mt-0.5">
                {t('diagnosingDesc', currentLanguage.code)}
              </p>
            </div>
          </div>
        )}

        {/* DIAGNOSIS RESULT CARD */}
        {diagnosis && !isAnalyzing && !isSearchingAi && (
          <div className="space-y-4 animate-in fade-in duration-300">
            {/* Header Result Card */}
            <div
              className={`p-5 rounded-3xl border shadow-sm ${
                diagnosis.isHealthy
                  ? 'bg-emerald-50/80 dark:bg-emerald-950/30 border-emerald-300 dark:border-emerald-800'
                  : 'bg-white dark:bg-neutral-800/90 border-neutral-200 dark:border-neutral-700'
              }`}
            >
              <div className="flex items-center justify-between gap-2 mb-3 flex-wrap">
                {/* Severity Badge & Varma Certified */}
                <div className="flex items-center gap-1.5 flex-wrap">
                  <span
                    className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold ${
                      diagnosis.isHealthy
                        ? 'bg-emerald-100 text-emerald-800 dark:bg-emerald-900/60 dark:text-emerald-300'
                        : diagnosis.severityLevel === 'Severe' || diagnosis.severityLevel === 'Critical'
                        ? 'bg-red-100 text-red-800 dark:bg-red-900/60 dark:text-red-300'
                        : 'bg-amber-100 text-amber-800 dark:bg-amber-900/60 dark:text-amber-300'
                    }`}
                  >
                    {diagnosis.isHealthy ? (
                      <CheckCircle className="w-3.5 h-3.5" />
                    ) : (
                      <AlertTriangle className="w-3.5 h-3.5" />
                    )}
                    {diagnosis.isHealthy
                      ? t('healthyBadge', currentLanguage.code)
                      : `${diagnosis.severityLevel} Severity`}
                  </span>

                  <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full bg-amber-400/20 text-amber-800 dark:text-amber-300 font-extrabold text-[11px] border border-amber-400/50 shadow-xs">
                    <span>🌾</span>
                    <span>Dr. Rohith Varma Protocol</span>
                  </span>
                </div>

                <span className="text-xs font-semibold px-2.5 py-1 rounded-full bg-blue-50 text-blue-700 dark:bg-blue-950/60 dark:text-blue-300 border border-blue-200/60">
                  {diagnosis.confidenceScore}% {t('matchScore', currentLanguage.code)}
                </span>
              </div>

              <h3 className="text-xl font-extrabold text-neutral-900 dark:text-white leading-tight">
                {diagnosis.diseaseName}
              </h3>
              <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 mt-1">
                {diagnosis.cropName} {diagnosis.cropScientificName && `(${diagnosis.cropScientificName})`}
              </p>

              {/* Farmer Summary */}
              <div className="mt-3.5 p-3 rounded-2xl bg-neutral-50 dark:bg-neutral-900/70 border border-neutral-100 dark:border-neutral-800 text-xs leading-relaxed text-neutral-700 dark:text-neutral-300">
                {diagnosis.summary}
              </div>

              {/* Action Buttons: Audio Voice & Save */}
              <div className="grid grid-cols-2 gap-2.5 mt-4">
                <button
                  onClick={toggleSpeech}
                  className={`py-2.5 px-3 rounded-xl flex items-center justify-center gap-2 font-bold text-xs transition ${
                    isSpeaking
                      ? 'bg-red-600 text-white hover:bg-red-700'
                      : 'bg-emerald-600 text-white hover:bg-emerald-700 shadow-sm'
                  }`}
                >
                  {isSpeaking ? <VolumeX className="w-4 h-4" /> : <Volume2 className="w-4 h-4" />}
                  {isSpeaking
                    ? t('stopVoice', currentLanguage.code)
                    : t('listenVoice', currentLanguage.code)}
                </button>

                <button
                  onClick={handleSave}
                  className="py-2.5 px-3 rounded-xl border border-neutral-300 dark:border-neutral-700 hover:bg-neutral-50 dark:hover:bg-neutral-800 text-neutral-700 dark:text-neutral-300 flex items-center justify-center gap-1.5 font-bold text-xs transition"
                >
                  <Bookmark className="w-4 h-4" />
                  {savedSuccess
                    ? t('savedSuccess', currentLanguage.code)
                    : t('saveDiagnosis', currentLanguage.code)}
                </button>
              </div>
            </div>

            {/* Symptoms list */}
            {diagnosis.symptoms.length > 0 && (
              <div className="p-4 rounded-2xl bg-white dark:bg-neutral-800/90 border border-neutral-200 dark:border-neutral-700 shadow-sm">
                <h4 className="text-xs font-bold text-neutral-800 dark:text-neutral-200 uppercase tracking-wider mb-2">
                  {t('symptomsTitle', currentLanguage.code)}
                </h4>
                <ul className="space-y-1.5">
                  {diagnosis.symptoms.map((symptom, i) => (
                    <li key={i} className="text-xs text-neutral-600 dark:text-neutral-300 flex items-start gap-2">
                      <span className="text-emerald-500 font-bold">•</span>
                      <span>{symptom}</span>
                    </li>
                  ))}
                </ul>
              </div>
            )}

            {/* Treatments Section: Organic vs Chemical Tabs */}
            <div className="p-5 rounded-3xl bg-white dark:bg-neutral-800/90 border border-neutral-200 dark:border-neutral-700 shadow-sm">
              <h4 className="text-sm font-extrabold text-neutral-900 dark:text-white mb-3">
                {t('treatmentTitle', currentLanguage.code)}
              </h4>

              {/* Tabs */}
              <div className="grid grid-cols-2 gap-1.5 p-1 bg-neutral-100 dark:bg-neutral-900 rounded-xl mb-4">
                <button
                  onClick={() => setTreatmentTab('organic')}
                  className={`py-2 text-xs font-bold rounded-lg transition flex items-center justify-center gap-1.5 ${
                    treatmentTab === 'organic'
                      ? 'bg-white dark:bg-neutral-800 text-emerald-700 dark:text-emerald-300 shadow-xs'
                      : 'text-neutral-500 hover:text-neutral-700'
                  }`}
                >
                  <Leaf className="w-3.5 h-3.5" />
                  {t('tabOrganic', currentLanguage.code)}
                </button>
                <button
                  onClick={() => setTreatmentTab('chemical')}
                  className={`py-2 text-xs font-bold rounded-lg transition flex items-center justify-center gap-1.5 ${
                    treatmentTab === 'chemical'
                      ? 'bg-white dark:bg-neutral-800 text-emerald-700 dark:text-emerald-300 shadow-xs'
                      : 'text-neutral-500 hover:text-neutral-700'
                  }`}
                >
                  <Pill className="w-3.5 h-3.5" />
                  {t('tabChemical', currentLanguage.code)}
                </button>
              </div>

              {treatmentTab === 'organic' ? (
                <div className="space-y-2.5">
                  {diagnosis.organicTreatments.map((org, idx) => (
                    <div
                      key={idx}
                      className="p-3 rounded-xl bg-emerald-50/70 dark:bg-emerald-950/30 border border-emerald-100 dark:border-emerald-900/60 flex items-start gap-2.5"
                    >
                      <span className="w-5 h-5 rounded-full bg-emerald-600 text-white font-bold text-[10px] flex items-center justify-center shrink-0 mt-0.5">
                        {idx + 1}
                      </span>
                      <p className="text-xs text-neutral-800 dark:text-neutral-200 leading-relaxed font-medium">
                        {org}
                      </p>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="space-y-3">
                  {diagnosis.chemicalTreatments.length === 0 ? (
                    <p className="text-xs text-neutral-500">No chemical treatments needed.</p>
                  ) : (
                    diagnosis.chemicalTreatments.map((chem, idx) => (
                      <div
                        key={idx}
                        className="p-3.5 rounded-2xl bg-neutral-50 dark:bg-neutral-900/60 border border-neutral-200 dark:border-neutral-800 space-y-1.5"
                      >
                        <div className="font-bold text-xs text-emerald-800 dark:text-emerald-300">
                          {chem.medicineName}
                        </div>
                        <div className="inline-block px-2 py-0.5 rounded-md bg-amber-50 text-amber-800 dark:bg-amber-950/60 dark:text-amber-300 font-bold text-[11px] border border-amber-200/60">
                          {t('dosageLabel', currentLanguage.code)} {chem.dosage}
                        </div>
                        <p className="text-xs text-neutral-600 dark:text-neutral-300 leading-relaxed">
                          {chem.instructions}
                        </p>
                        {chem.safetyWarning && (
                          <div className="flex items-center gap-1.5 text-[11px] text-red-600 dark:text-red-400 font-medium pt-1">
                            <span>⚠️ {chem.safetyWarning}</span>
                          </div>
                        )}
                      </div>
                    ))
                  )}
                </div>
              )}
            </div>

            {/* Spray Window Advisory */}
            {diagnosis.sprayWindowRecommendation && (
              <div className="p-4 rounded-2xl bg-blue-50 dark:bg-blue-950/40 border border-blue-200 dark:border-blue-900 flex items-center gap-3">
                <Clock className="w-5 h-5 text-blue-600 shrink-0" />
                <div>
                  <div className="font-bold text-xs text-blue-900 dark:text-blue-200">
                    {t('sprayWindowLabel', currentLanguage.code)}
                  </div>
                  <div className="text-xs text-neutral-700 dark:text-neutral-300 mt-0.5">
                    {diagnosis.sprayWindowRecommendation}
                  </div>
                </div>
              </div>
            )}
          </div>
        )}
      </div>

      {/* Camera Capture Modal */}
      {showCamera && (
        <CameraCaptureModal
          language={currentLanguage}
          onCapture={handleCapture}
          onClose={() => setShowCamera(false)}
          onUploadFallback={() => fileInputRef.current?.click()}
        />
      )}
    </div>
  );
};
