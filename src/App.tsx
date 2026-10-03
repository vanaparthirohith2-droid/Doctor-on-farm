import React, { useState } from 'react';
import {
  CloudSun,
  CalendarDays,
  History as HistoryIcon,
  Globe,
  Leaf
} from 'lucide-react';
import { AppLanguage, DiseaseDiagnosis } from './types';
import { APP_LANGUAGES, getOfflineDiagnosis } from './data/mockData';
import { CropDoctorTab } from './components/CropDoctorTab';
import { WeatherTab } from './components/WeatherTab';
import { SeasonalCropsTab } from './components/SeasonalCropsTab';
import { HistoryTab } from './components/HistoryTab';
import { LanguageSelectorModal } from './components/LanguageSelectorModal';
import { t } from './utils/translations';

export function App() {
  const [currentLanguage, setCurrentLanguage] = useState<AppLanguage>(APP_LANGUAGES[0]); // Hindi default
  const [activeTab, setActiveTab] = useState<'DOCTOR' | 'WEATHER' | 'SEASONS' | 'HISTORY'>('DOCTOR');
  const [showLanguageModal, setShowLanguageModal] = useState(false);

  // Pre-seed history with two realistic scans with guaranteed unique IDs
  const [savedScans, setSavedScans] = useState<DiseaseDiagnosis[]>(() => [
    { ...getOfflineDiagnosis('tomato_early_blight', 'hi'), id: 'seed_scan_tomato_early_blight_01' },
    { ...getOfflineDiagnosis('wheat_yellow_rust', 'hi'), id: 'seed_scan_wheat_yellow_rust_02' }
  ]);

  const handleSaveToHistory = (diag: DiseaseDiagnosis) => {
    const uniqueSavedItem: DiseaseDiagnosis = {
      ...diag,
      id: `saved_${Date.now()}_${Math.random().toString(36).substring(2, 9)}`
    };
    setSavedScans(prev => [uniqueSavedItem, ...prev.filter(d => d.id !== uniqueSavedItem.id)]);
  };

  const navItems = [
    {
      id: 'DOCTOR' as const,
      labelKey: 'tabDoctor' as const,
      icon: Leaf
    },
    {
      id: 'WEATHER' as const,
      labelKey: 'tabWeather' as const,
      icon: CloudSun
    },
    {
      id: 'SEASONS' as const,
      labelKey: 'tabSeasons' as const,
      icon: CalendarDays
    },
    {
      id: 'HISTORY' as const,
      labelKey: 'tabHistory' as const,
      icon: HistoryIcon
    }
  ];

  return (
    <div className="min-h-screen bg-[#F8FAF7] text-[#1A211B] flex justify-center">
      {/* Mobile-optimized viewport container */}
      <div className="w-full max-w-md bg-white min-h-screen shadow-xl flex flex-col relative border-x border-neutral-100">
        {/* Top Branding & Language Bar */}
        <div className="sticky top-0 z-40 bg-emerald-950 text-white px-4 py-2.5 flex items-center justify-between shadow-xs">
          <div className="flex items-center gap-2.5">
            <img
              src="/logo.jpg"
              alt="Doctor on Farm Logo"
              className="w-9 h-9 rounded-xl object-cover ring-2 ring-amber-400 shadow-md shrink-0"
            />
            <div>
              <div className="text-sm font-black tracking-tight text-white leading-tight flex items-center gap-1.5">
                <span>Doctor on Farm</span>
                <span className="px-1.5 py-0.5 rounded-md bg-gradient-to-r from-amber-400 to-yellow-300 text-neutral-950 text-[10px] font-black uppercase tracking-wider shadow-sm ring-1 ring-amber-300">
                  ROHITH VARMA
                </span>
              </div>
              <div className="flex items-center gap-1.5 mt-0.5">
                <span className="text-[10px] text-emerald-300/90 font-medium">
                  {t('appSubtitle', currentLanguage.code)}
                </span>
                <span className="text-[10px] text-amber-300 font-bold">• Dr. Rohith Varma</span>
              </div>
            </div>
          </div>

          <div className="flex items-center gap-2">
            {/* Highlighted Rohith Varma badge */}
            <div className="flex items-center gap-1 px-2.5 py-1 rounded-full bg-gradient-to-r from-amber-500/25 to-yellow-500/20 border border-amber-400/80 text-amber-300 text-[11px] font-black shadow-xs">
              <span className="text-amber-400 text-xs">🌾</span>
              <span className="tracking-wide">Rohith Varma</span>
            </div>

            {/* Language Selector Pill */}
            <button
              onClick={() => setShowLanguageModal(true)}
              className="flex items-center gap-1.5 px-3 py-1.5 rounded-full bg-emerald-800/80 hover:bg-emerald-700 text-white text-xs font-bold transition border border-emerald-600/60 active:scale-95"
              title="Change Language"
            >
              <Globe className="w-3.5 h-3.5 text-emerald-300" />
              <span>
                {currentLanguage.flag} {currentLanguage.nativeName}
              </span>
            </button>
          </div>
        </div>

        {/* Tab Content */}
        <div className="flex-1 overflow-y-auto">
          {activeTab === 'DOCTOR' && (
            <CropDoctorTab
              currentLanguage={currentLanguage}
              onSaveToHistory={handleSaveToHistory}
            />
          )}

          {activeTab === 'WEATHER' && (
            <WeatherTab currentLanguage={currentLanguage} />
          )}

          {activeTab === 'SEASONS' && (
            <SeasonalCropsTab currentLanguage={currentLanguage} />
          )}

          {activeTab === 'HISTORY' && (
            <HistoryTab
              savedScans={savedScans}
              currentLanguage={currentLanguage}
              onClearHistory={() => setSavedScans([])}
            />
          )}
        </div>

        {/* Bottom Navigation Bar */}
        <div className="fixed bottom-0 left-0 right-0 z-40 flex justify-center pointer-events-none">
          <nav className="w-full max-w-md bg-white/95 backdrop-blur-md border-t border-neutral-200 px-2 py-2 flex items-center justify-around shadow-2xl pointer-events-auto">
            {navItems.map(item => {
              const Icon = item.icon;
              const isActive = activeTab === item.id;
              return (
                <button
                  key={item.id}
                  onClick={() => setActiveTab(item.id)}
                  className={`flex flex-col items-center justify-center py-1 px-3 rounded-2xl transition duration-150 relative ${
                    isActive
                      ? 'text-emerald-700 font-extrabold'
                      : 'text-neutral-500 hover:text-neutral-800'
                  }`}
                >
                  <div
                    className={`p-1.5 rounded-xl transition ${
                      isActive ? 'bg-emerald-100 text-emerald-800' : ''
                    }`}
                  >
                    <Icon className="w-5 h-5" />
                  </div>
                  <span className="text-[10px] mt-0.5 tracking-tight font-semibold">
                    {t(item.labelKey, currentLanguage.code)}
                  </span>
                </button>
              );
            })}
          </nav>
        </div>

        {/* Language Modal */}
        {showLanguageModal && (
          <LanguageSelectorModal
            currentLanguage={currentLanguage}
            onSelect={lang => setCurrentLanguage(lang)}
            onClose={() => setShowLanguageModal(false)}
          />
        )}
      </div>
    </div>
  );
}

export default App;
