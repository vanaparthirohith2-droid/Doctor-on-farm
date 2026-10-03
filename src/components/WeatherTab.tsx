import React, { useState, useEffect } from 'react';
import {
  Cloud,
  Droplets,
  Wind,
  CheckCircle2,
  AlertTriangle,
  MapPin,
  RefreshCw,
  Sun,
  ShieldAlert
} from 'lucide-react';
import { AgriculturalWeather, AppLanguage } from '../types';
import { POPULAR_REGIONS, FarmLocation, fetchAgriculturalWeather } from '../services/weatherService';
import { t } from '../utils/translations';

interface WeatherTabProps {
  currentLanguage: AppLanguage;
}

export const WeatherTab: React.FC<WeatherTabProps> = ({ currentLanguage }) => {
  const [selectedLocation, setSelectedLocation] = useState<FarmLocation>(POPULAR_REGIONS[0]);
  const [weather, setWeather] = useState<AgriculturalWeather | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const loadWeather = async (loc: FarmLocation) => {
    setIsLoading(true);
    try {
      const data = await fetchAgriculturalWeather(loc);
      setWeather(data);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadWeather(selectedLocation);
  }, [selectedLocation]);

  return (
    <div className="pb-28">
      {/* Header */}
      <div className="bg-gradient-to-r from-blue-700 to-indigo-800 text-white px-5 py-6 rounded-b-3xl shadow-sm">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 rounded-2xl bg-white/15 flex items-center justify-center backdrop-blur-sm">
              <Cloud className="w-7 h-7 text-blue-200" />
            </div>
            <div>
              <h1 className="text-xl font-extrabold tracking-tight">
                {t('weatherTitle', currentLanguage.code)}
              </h1>
              <p className="text-xs text-blue-100 font-medium">
                {selectedLocation.name}, {selectedLocation.state}
              </p>
            </div>
          </div>

          <button
            onClick={() => loadWeather(selectedLocation)}
            className="p-2.5 rounded-full bg-white/10 hover:bg-white/20 transition active:scale-95"
          >
            <RefreshCw className={`w-4 h-4 text-white ${isLoading ? 'animate-spin' : ''}`} />
          </button>
        </div>
      </div>

      <div className="px-4 mt-5 space-y-4">
        {/* District selector chips */}
        <div>
          <span className="text-xs font-bold text-neutral-500 uppercase tracking-wider block mb-2">
            {t('selectDistrict', currentLanguage.code)}
          </span>
          <div className="flex gap-2 overflow-x-auto pb-1 scrollbar-none">
            {POPULAR_REGIONS.map(reg => {
              const isSelected = reg.name === selectedLocation.name;
              return (
                <button
                  key={reg.name}
                  onClick={() => setSelectedLocation(reg)}
                  className={`shrink-0 px-3.5 py-1.5 rounded-full text-xs font-semibold flex items-center gap-1.5 transition ${
                    isSelected
                      ? 'bg-blue-600 text-white shadow-xs'
                      : 'bg-white dark:bg-neutral-800 text-neutral-700 dark:text-neutral-300 border border-neutral-200 dark:border-neutral-700 hover:border-blue-300'
                  }`}
                >
                  <MapPin className="w-3 h-3" />
                  {reg.name}
                </button>
              );
            })}
          </div>
        </div>

        {/* Current Weather Card */}
        {weather && (
          <>
            <div className="p-5 rounded-3xl bg-white dark:bg-neutral-800/90 border border-neutral-200 dark:border-neutral-700 shadow-sm">
              <div className="flex items-center justify-between">
                <div>
                  <div className="text-4xl font-black text-neutral-900 dark:text-white">
                    {weather.currentTemp}°C
                  </div>
                  <div className="text-xs font-medium text-neutral-500 dark:text-neutral-400 mt-1">
                    {weather.weatherCondition} • Feels like {weather.feelsLike}°C
                  </div>
                </div>
                <div className="text-5xl">{weather.weatherIcon}</div>
              </div>

              {/* Metrics grid */}
              <div className="grid grid-cols-3 gap-2.5 mt-5">
                <div className="p-3 rounded-2xl bg-blue-50/80 dark:bg-blue-950/30 text-center border border-blue-100 dark:border-blue-900/50">
                  <Droplets className="w-5 h-5 text-blue-600 mx-auto mb-1" />
                  <div className="font-bold text-sm text-neutral-900 dark:text-white">
                    {weather.humidity}%
                  </div>
                  <div className="text-[10px] text-neutral-500 font-medium">
                    {t('humidityLabel', currentLanguage.code)}
                  </div>
                </div>

                <div className="p-3 rounded-2xl bg-emerald-50/80 dark:bg-emerald-950/30 text-center border border-emerald-100 dark:border-emerald-900/50">
                  <Wind className="w-5 h-5 text-emerald-600 mx-auto mb-1" />
                  <div className="font-bold text-sm text-neutral-900 dark:text-white">
                    {weather.windSpeed.toFixed(1)} km/h
                  </div>
                  <div className="text-[10px] text-neutral-500 font-medium">
                    {t('windLabel', currentLanguage.code)}
                  </div>
                </div>

                <div className="p-3 rounded-2xl bg-amber-50/80 dark:bg-amber-950/30 text-center border border-amber-100 dark:border-amber-900/50">
                  <Droplets className="w-5 h-5 text-amber-600 mx-auto mb-1" />
                  <div className="font-bold text-sm text-neutral-900 dark:text-white">
                    {weather.rainfallChance}%
                  </div>
                  <div className="text-[10px] text-neutral-500 font-medium">
                    {t('rainLabel', currentLanguage.code)}
                  </div>
                </div>
              </div>
            </div>

            {/* Spray Window Advisory */}
            <div
              className={`p-4 rounded-2xl border ${
                weather.sprayStatus === 'FAVORABLE'
                  ? 'bg-emerald-50 dark:bg-emerald-950/30 border-emerald-300 dark:border-emerald-800'
                  : weather.sprayStatus === 'CAUTION'
                  ? 'bg-amber-50 dark:bg-amber-950/30 border-amber-300 dark:border-amber-800'
                  : 'bg-red-50 dark:bg-red-950/30 border-red-300 dark:border-red-800'
              }`}
            >
              <div className="flex items-center gap-2 mb-1.5">
                {weather.sprayStatus === 'FAVORABLE' ? (
                  <CheckCircle2 className="w-5 h-5 text-emerald-600" />
                ) : (
                  <AlertTriangle className="w-5 h-5 text-amber-600" />
                )}
                <span
                  className={`font-bold text-sm ${
                    weather.sprayStatus === 'FAVORABLE'
                      ? 'text-emerald-900 dark:text-emerald-200'
                      : weather.sprayStatus === 'CAUTION'
                      ? 'text-amber-900 dark:text-amber-200'
                      : 'text-red-900 dark:text-red-200'
                  }`}
                >
                  {weather.sprayStatus === 'FAVORABLE'
                    ? t('safeToSpray', currentLanguage.code)
                    : weather.sprayStatus === 'CAUTION'
                    ? t('cautionSpray', currentLanguage.code)
                    : t('doNotSpray', currentLanguage.code)}
                </span>
              </div>
              <p className="text-xs text-neutral-700 dark:text-neutral-300 leading-relaxed font-medium">
                {weather.sprayReason}
              </p>
            </div>

            {/* Irrigation & Disease Risk Guidance */}
            <div className="p-4 rounded-2xl bg-white dark:bg-neutral-800/90 border border-neutral-200 dark:border-neutral-700 space-y-3 shadow-sm">
              <div className="flex items-start gap-3">
                <div className="w-8 h-8 rounded-xl bg-blue-100 dark:bg-blue-900/40 text-blue-700 dark:text-blue-300 flex items-center justify-center shrink-0">
                  <Droplets className="w-4 h-4" />
                </div>
                <div>
                  <h4 className="font-bold text-xs text-neutral-900 dark:text-white">
                    {t('irrigationTitle', currentLanguage.code)}
                  </h4>
                  <p className="text-xs text-neutral-600 dark:text-neutral-300 mt-0.5 leading-relaxed">
                    {weather.irrigationAdvice}
                  </p>
                </div>
              </div>

              <div className="border-t border-neutral-100 dark:border-neutral-800 pt-3 flex items-start gap-3">
                <div className="w-8 h-8 rounded-xl bg-amber-100 dark:bg-amber-900/40 text-amber-700 dark:text-amber-300 flex items-center justify-center shrink-0">
                  <ShieldAlert className="w-4 h-4" />
                </div>
                <div>
                  <h4 className="font-bold text-xs text-neutral-900 dark:text-white">
                    {t('diseaseRiskTitle', currentLanguage.code)}
                  </h4>
                  <p className="text-xs text-neutral-600 dark:text-neutral-300 mt-0.5 leading-relaxed">
                    {weather.diseaseRiskAlert}
                  </p>
                </div>
              </div>
            </div>

            {/* 5-Day Forecast */}
            <div className="p-4 rounded-2xl bg-white dark:bg-neutral-800/90 border border-neutral-200 dark:border-neutral-700 shadow-sm">
              <h4 className="font-bold text-xs text-neutral-900 dark:text-white uppercase tracking-wider mb-3">
                {t('forecastTitle', currentLanguage.code)}
              </h4>

              <div className="space-y-2">
                {weather.forecast.map((day, idx) => (
                  <div
                    key={idx}
                    className="flex items-center justify-between p-2.5 rounded-xl bg-neutral-50 dark:bg-neutral-900/50 text-xs"
                  >
                    <div className="flex items-center gap-3">
                      <span className="text-xl">{day.iconEmoji}</span>
                      <div>
                        <div className="font-bold text-neutral-900 dark:text-white">
                          {day.dayName}
                        </div>
                        <div className="text-[10px] text-neutral-500">{day.condition}</div>
                      </div>
                    </div>

                    <div className="flex items-center gap-3">
                      <span className="px-2 py-0.5 rounded-md bg-blue-100/70 text-blue-800 dark:bg-blue-900/60 dark:text-blue-300 text-[10px] font-semibold">
                        💧 {day.rainProb}%
                      </span>
                      <span className="font-bold text-neutral-800 dark:text-neutral-200">
                        {day.maxTemp}° / {day.minTemp}°
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </>
        )}
      </div>
    </div>
  );
};
