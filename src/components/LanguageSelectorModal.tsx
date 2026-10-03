import React from 'react';
import { X, Check } from 'lucide-react';
import { AppLanguage } from '../types';
import { APP_LANGUAGES } from '../data/mockData';

interface LanguageSelectorModalProps {
  currentLanguage: AppLanguage;
  onSelect: (lang: AppLanguage) => void;
  onClose: () => void;
}

export const LanguageSelectorModal: React.FC<LanguageSelectorModalProps> = ({
  currentLanguage,
  onSelect,
  onClose
}) => {
  return (
    <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-sm flex items-end sm:items-center justify-center p-0 sm:p-4">
      <div className="bg-white dark:bg-neutral-900 w-full sm:max-w-md rounded-t-3xl sm:rounded-3xl p-6 shadow-2xl animate-in slide-in-from-bottom duration-200">
        <div className="flex items-center justify-between pb-4 border-b border-neutral-100 dark:border-neutral-800">
          <div>
            <h3 className="text-lg font-bold text-neutral-900 dark:text-white">अपनी भाषा चुनें</h3>
            <p className="text-xs text-neutral-500">Select Your Language</p>
          </div>
          <button
            onClick={onClose}
            className="p-2 rounded-full hover:bg-neutral-100 dark:hover:bg-neutral-800 text-neutral-500"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="grid grid-cols-2 gap-2.5 mt-4 max-h-80 overflow-y-auto pr-1">
          {APP_LANGUAGES.map(lang => {
            const isSelected = lang.code === currentLanguage.code;
            return (
              <button
                key={lang.code}
                onClick={() => {
                  onSelect(lang);
                  onClose();
                }}
                className={`p-3 rounded-2xl flex items-center justify-between text-left transition border ${
                  isSelected
                    ? 'bg-emerald-50 dark:bg-emerald-950/40 border-emerald-500 text-emerald-900 dark:text-emerald-300'
                    : 'bg-neutral-50 dark:bg-neutral-800/60 border-neutral-200 dark:border-neutral-700/60 hover:border-neutral-300 text-neutral-800 dark:text-neutral-200'
                }`}
              >
                <div className="flex items-center gap-2.5">
                  <span className="text-xl">{lang.flag}</span>
                  <div>
                    <div className="font-bold text-sm leading-tight">{lang.nativeName}</div>
                    <div className="text-[10px] text-neutral-500">{lang.englishName}</div>
                  </div>
                </div>
                {isSelected && (
                  <div className="w-5 h-5 rounded-full bg-emerald-600 flex items-center justify-center text-white">
                    <Check className="w-3 h-3" />
                  </div>
                )}
              </button>
            );
          })}
        </div>
      </div>
    </div>
  );
};
