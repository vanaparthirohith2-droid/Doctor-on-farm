import React, { useState, useMemo } from 'react';
import {
  Calendar,
  Search,
  Clock,
  TrendingUp
} from 'lucide-react';
import { AppLanguage, CropSeason, CropSuggestion } from '../types';
import { SEASONAL_CROPS } from '../data/mockData';
import { t } from '../utils/translations';

interface SeasonalCropsTabProps {
  currentLanguage: AppLanguage;
}

export const SeasonalCropsTab: React.FC<SeasonalCropsTabProps> = ({ currentLanguage }) => {
  const [selectedSeason, setSelectedSeason] = useState<CropSeason>('ALL');
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedSoil, setSelectedSoil] = useState('All');
  const [expandedCropId, setExpandedCropId] = useState<string | null>(null);

  const soilFilters = ['All', 'Loam', 'Black Soil', 'Sandy Loam', 'Clay', 'Alluvial'];

  const filteredCrops = useMemo(() => {
    return SEASONAL_CROPS.filter(crop => {
      const matchSeason = selectedSeason === 'ALL' || crop.season === selectedSeason;
      const matchSearch =
        crop.nameEn.toLowerCase().includes(searchQuery.toLowerCase()) ||
        crop.nameHi.includes(searchQuery) ||
        crop.category.toLowerCase().includes(searchQuery.toLowerCase());
      const matchSoil =
        selectedSoil === 'All' || crop.soilTypes.some(s => s.toLowerCase().includes(selectedSoil.toLowerCase()));

      return matchSeason && matchSearch && matchSoil;
    });
  }, [selectedSeason, searchQuery, selectedSoil]);

  return (
    <div className="pb-28">
      {/* Header */}
      <div className="bg-gradient-to-r from-amber-600 to-amber-700 text-white px-5 py-6 rounded-b-3xl shadow-sm">
        <div className="flex items-center gap-3">
          <div className="w-12 h-12 rounded-2xl bg-white/15 flex items-center justify-center backdrop-blur-sm">
            <Calendar className="w-7 h-7 text-amber-100" />
          </div>
          <div>
            <h1 className="text-xl font-extrabold tracking-tight">
              {t('seasonsTitle', currentLanguage.code)}
            </h1>
            <p className="text-xs text-amber-100 font-medium">
              {t('seasonsSubtitle', currentLanguage.code)}
            </p>
          </div>
        </div>
      </div>

      <div className="px-4 mt-5 space-y-4">
        {/* Season Tabs */}
        <div className="grid grid-cols-4 gap-1.5 p-1 bg-neutral-100 dark:bg-neutral-800/80 rounded-2xl">
          {[
            { id: 'ALL' as const, label: t('seasonAll', currentLanguage.code) },
            { id: 'KHARIF' as const, label: t('seasonKharif', currentLanguage.code).split(' ')[0] },
            { id: 'RABI' as const, label: t('seasonRabi', currentLanguage.code).split(' ')[0] },
            { id: 'ZAID' as const, label: t('seasonZaid', currentLanguage.code).split(' ')[0] }
          ].map(tab => {
            const isSelected = selectedSeason === tab.id;
            return (
              <button
                key={tab.id}
                onClick={() => setSelectedSeason(tab.id)}
                className={`py-2 px-1 text-center rounded-xl text-xs font-bold transition ${
                  isSelected
                    ? 'bg-white dark:bg-neutral-700 text-amber-700 dark:text-amber-300 shadow-xs'
                    : 'text-neutral-500 hover:text-neutral-800'
                }`}
              >
                <div>{tab.label}</div>
              </button>
            );
          })}
        </div>

        {/* Search bar */}
        <div className="relative">
          <Search className="w-4 h-4 text-neutral-400 absolute left-3.5 top-3" />
          <input
            type="text"
            value={searchQuery}
            onChange={e => setSearchQuery(e.target.value)}
            placeholder={t('searchCropsPlaceholder', currentLanguage.code)}
            className="w-full pl-10 pr-4 py-2.5 rounded-2xl bg-white dark:bg-neutral-800 border border-neutral-200 dark:border-neutral-700 text-xs text-neutral-900 dark:text-white placeholder:text-neutral-400 focus:outline-none focus:border-amber-500"
          />
        </div>

        {/* Soil Filter Chips */}
        <div>
          <span className="text-[11px] font-semibold text-neutral-500 block mb-1.5">
            {t('soilFilterLabel', currentLanguage.code)}
          </span>
          <div className="flex gap-2 overflow-x-auto pb-1 scrollbar-none">
            {soilFilters.map(soil => {
              const isSelected = selectedSoil === soil;
              return (
                <button
                  key={soil}
                  onClick={() => setSelectedSoil(soil)}
                  className={`shrink-0 px-3 py-1 rounded-full text-xs font-semibold transition ${
                    isSelected
                      ? 'bg-amber-600 text-white'
                      : 'bg-neutral-100 dark:bg-neutral-800 text-neutral-700 dark:text-neutral-300 hover:bg-neutral-200'
                  }`}
                >
                  {soil === 'All' ? t('soilAll', currentLanguage.code) : soil}
                </button>
              );
            })}
          </div>
        </div>

        {/* Crops Count */}
        <div className="flex items-center justify-between text-xs text-neutral-500">
          <span className="font-bold text-neutral-800 dark:text-neutral-200">
            {filteredCrops.length} {currentLanguage.code === 'hi' ? 'फसलें उपलब्ध' : 'crops available'}
          </span>
          <span>Tap card for details</span>
        </div>

        {/* Crops List */}
        <div className="space-y-3">
          {filteredCrops.map(crop => {
            const isExpanded = expandedCropId === crop.id;
            return (
              <div
                key={crop.id}
                onClick={() => setExpandedCropId(isExpanded ? null : crop.id)}
                className="p-4 rounded-3xl bg-white dark:bg-neutral-800/90 border border-neutral-200 dark:border-neutral-700 shadow-sm cursor-pointer hover:border-amber-300 transition"
              >
                <div className="flex items-start justify-between">
                  <div>
                    <h3 className="text-base font-extrabold text-neutral-900 dark:text-white">
                      {currentLanguage.code === 'hi' ? crop.nameHi : crop.nameEn}
                    </h3>
                    <p className="text-xs text-neutral-500 font-medium">
                      {currentLanguage.code === 'hi' ? crop.nameEn : crop.nameHi} • {crop.category}
                    </p>
                  </div>

                  <span
                    className={`px-2.5 py-1 rounded-full text-[11px] font-bold ${
                      crop.season === 'KHARIF'
                        ? 'bg-emerald-100 text-emerald-800'
                        : crop.season === 'RABI'
                        ? 'bg-blue-100 text-blue-800'
                        : 'bg-amber-100 text-amber-800'
                    }`}
                  >
                    {crop.season === 'KHARIF'
                      ? t('seasonKharif', currentLanguage.code)
                      : crop.season === 'RABI'
                      ? t('seasonRabi', currentLanguage.code)
                      : t('seasonZaid', currentLanguage.code)}
                  </span>
                </div>

                {/* Key stats */}
                <div className="grid grid-cols-2 gap-2 mt-3 pt-3 border-t border-neutral-100 dark:border-neutral-800 text-xs">
                  <div className="flex items-center gap-1.5 text-neutral-600 dark:text-neutral-300">
                    <Clock className="w-3.5 h-3.5 text-neutral-400" />
                    <span>{t('durationLabel', currentLanguage.code)}: {crop.harvestingDurationDays} days</span>
                  </div>

                  <div className="flex items-center gap-1.5 text-emerald-600 font-semibold">
                    <TrendingUp className="w-3.5 h-3.5" />
                    <span>{t('yieldLabel', currentLanguage.code)}: {crop.estimatedYield}</span>
                  </div>
                </div>

                <div className="flex items-center justify-between mt-2 pt-2 text-[11px] text-neutral-500">
                  <span>{t('sowingLabel', currentLanguage.code)}: {crop.sowingMonths}</span>
                  <span className="font-bold text-amber-600">
                    {t('profitLabel', currentLanguage.code)}: {crop.profitPotential}
                  </span>
                </div>

                {/* Expanded agronomic details */}
                {isExpanded && (
                  <div className="mt-3.5 pt-3.5 border-t border-neutral-200 dark:border-neutral-700 space-y-2.5 text-xs">
                    <div>
                      <span className="font-bold text-neutral-700 dark:text-neutral-300 block">
                        {t('soilTypeLabel', currentLanguage.code)}:
                      </span>
                      <span className="text-neutral-600 dark:text-neutral-400">
                        {crop.soilTypes.join(', ')}
                      </span>
                    </div>

                    <div>
                      <span className="font-bold text-neutral-700 dark:text-neutral-300 block">
                        {t('seedRateLabel', currentLanguage.code)}:
                      </span>
                      <span className="text-neutral-600 dark:text-neutral-400">
                        {crop.seedRate} • {crop.spacing}
                      </span>
                    </div>

                    <div className="p-3 rounded-2xl bg-amber-50 dark:bg-amber-950/30 border border-amber-200/60 dark:border-amber-900/40">
                      <div className="flex items-center gap-1.5 mb-1">
                        <span className="font-bold text-amber-950 dark:text-amber-100 text-xs">
                          {t('expertAdviceLabel', currentLanguage.code)}
                        </span>
                        <span className="px-1.5 py-0.2 rounded bg-amber-400 text-neutral-950 font-black text-[9px] uppercase tracking-wider shadow-xs">
                          ROHITH VARMA
                        </span>
                      </div>
                      <p className="text-neutral-700 dark:text-neutral-300 leading-relaxed font-medium">
                        {crop.keyTips}
                      </p>
                    </div>

                    {crop.pestResistantVarieties.length > 0 && (
                      <div>
                        <span className="font-bold text-emerald-700 dark:text-emerald-400 block">
                          {t('resistantVarietiesLabel', currentLanguage.code)}:
                        </span>
                        <span className="text-neutral-600 dark:text-neutral-400 font-medium">
                          {crop.pestResistantVarieties.join(', ')}
                        </span>
                      </div>
                    )}
                  </div>
                )}
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};
