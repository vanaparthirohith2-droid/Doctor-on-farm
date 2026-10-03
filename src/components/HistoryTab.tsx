import React from 'react';
import { History, Volume2, Trash2, CheckCircle2, AlertTriangle } from 'lucide-react';
import { AppLanguage, DiseaseDiagnosis } from '../types';
import { t } from '../utils/translations';

interface HistoryTabProps {
  savedScans: DiseaseDiagnosis[];
  currentLanguage: AppLanguage;
  onClearHistory: () => void;
}

export const HistoryTab: React.FC<HistoryTabProps> = ({
  savedScans,
  currentLanguage,
  onClearHistory
}) => {
  const playAudio = (item: DiseaseDiagnosis) => {
    if (!('speechSynthesis' in window)) return;
    window.speechSynthesis.cancel();

    const narrationText = item.isHealthy
      ? `${item.cropName}। ${t('healthyBadge', currentLanguage.code)}। ${item.summary}`
      : `${item.cropName}। ${item.diseaseName}। ${item.summary}।`;

    const utterance = new SpeechSynthesisUtterance(narrationText);
    utterance.lang = currentLanguage.ttsVoiceLang;
    utterance.rate = 0.9;
    window.speechSynthesis.speak(utterance);
  };

  return (
    <div className="pb-28">
      {/* Header */}
      <div className="bg-gradient-to-r from-emerald-800 to-teal-800 text-white px-5 py-6 rounded-b-3xl shadow-sm">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 rounded-2xl bg-white/15 flex items-center justify-center backdrop-blur-sm">
              <History className="w-7 h-7 text-emerald-200" />
            </div>
            <div>
              <h1 className="text-xl font-extrabold tracking-tight">
                {t('historyTitle', currentLanguage.code)}
              </h1>
              <p className="text-xs text-emerald-100 font-medium">
                {savedScans.length} {t('historyCount', currentLanguage.code)}
              </p>
            </div>
          </div>

          {savedScans.length > 0 && (
            <button
              onClick={onClearHistory}
              className="p-2.5 rounded-full bg-white/10 hover:bg-white/20 text-white transition active:scale-95"
              title={t('clearHistory', currentLanguage.code)}
            >
              <Trash2 className="w-4 h-4" />
            </button>
          )}
        </div>
      </div>

      <div className="px-4 mt-5">
        {savedScans.length === 0 ? (
          <div className="py-16 text-center">
            <div className="w-16 h-16 rounded-full bg-neutral-100 dark:bg-neutral-800 text-neutral-400 flex items-center justify-center mx-auto mb-3">
              <History className="w-8 h-8" />
            </div>
            <h3 className="font-bold text-neutral-800 dark:text-neutral-200 text-sm mb-1">
              {t('noHistoryTitle', currentLanguage.code)}
            </h3>
            <p className="text-xs text-neutral-500 max-w-xs mx-auto leading-relaxed">
              {t('noHistoryDesc', currentLanguage.code)}
            </p>
          </div>
        ) : (
          <div className="space-y-3">
            {savedScans.map((item, index) => (
              <div
                key={item.id ? `${item.id}-${index}` : `scan-${index}`}
                className="p-4 rounded-3xl bg-white dark:bg-neutral-800/90 border border-neutral-200 dark:border-neutral-700 shadow-sm"
              >
                <div className="flex items-start justify-between">
                  <div>
                    <span className="text-[10px] text-neutral-400 font-medium block mb-1">
                      {new Date(item.timestamp).toLocaleString(currentLanguage.code === 'hi' ? 'hi-IN' : 'en-US', {
                        day: 'numeric',
                        month: 'short',
                        year: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit'
                      })}
                    </span>
                    <h3 className="text-base font-extrabold text-neutral-900 dark:text-white">
                      {item.diseaseName}
                    </h3>
                    <p className="text-xs text-neutral-500 font-medium">
                      {item.cropName} {item.scientificName && `• ${item.scientificName}`}
                    </p>
                  </div>

                  <span
                    className={`inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-[11px] font-bold ${
                      item.isHealthy
                        ? 'bg-emerald-100 text-emerald-800'
                        : 'bg-red-100 text-red-800'
                    }`}
                  >
                    {item.isHealthy ? (
                      <CheckCircle2 className="w-3 h-3" />
                    ) : (
                      <AlertTriangle className="w-3 h-3" />
                    )}
                    {item.isHealthy ? t('healthyBadge', currentLanguage.code) : item.severityLevel}
                  </span>
                </div>

                <p className="text-xs text-neutral-600 dark:text-neutral-300 mt-2.5 leading-relaxed line-clamp-2">
                  {item.summary}
                </p>

                <div className="flex justify-end mt-3 pt-2.5 border-t border-neutral-100 dark:border-neutral-800">
                  <button
                    onClick={() => playAudio(item)}
                    className="flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-emerald-50 dark:bg-emerald-950/40 text-emerald-700 dark:text-emerald-300 font-bold text-xs hover:bg-emerald-100 transition active:scale-95"
                  >
                    <Volume2 className="w-3.5 h-3.5" />
                    {t('listenAgain', currentLanguage.code)}
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
