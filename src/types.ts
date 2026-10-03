export interface AppLanguage {
  code: string;
  englishName: string;
  nativeName: string;
  flag: string;
  ttsVoiceLang: string;
}

export interface TreatmentDetail {
  medicineName: string;
  dosage: string;
  instructions: string;
  safetyWarning?: string;
}

export interface DiseaseDiagnosis {
  id: string;
  cropName: string;
  cropScientificName?: string;
  diseaseName: string;
  scientificName?: string;
  isHealthy: boolean;
  severityLevel: 'Healthy' | 'Low' | 'Moderate' | 'High' | 'Severe' | 'Critical';
  confidenceScore: number;
  summary: string;
  symptoms: string[];
  causes: string[];
  organicTreatments: string[];
  chemicalTreatments: TreatmentDetail[];
  preventiveMeasures: string[];
  sprayWindowRecommendation: string;
  languageCode: string;
  imageUrl?: string;
  timestamp: number;
}

export type CropSeason = 'ALL' | 'KHARIF' | 'RABI' | 'ZAID';

export interface CropSuggestion {
  id: string;
  nameEn: string;
  nameHi: string;
  category: string;
  season: CropSeason;
  seasonLabelHi: string;
  sowingMonths: string;
  harvestingDurationDays: number;
  soilTypes: string[];
  waterRequirement: string;
  estimatedYield: string;
  profitPotential: 'High' | 'Very High' | 'Moderate';
  seedRate: string;
  spacing: string;
  keyTips: string;
  companionCrops: string[];
  pestResistantVarieties: string[];
}

export interface DailyForecast {
  dayName: string;
  date: string;
  maxTemp: number;
  minTemp: number;
  condition: string;
  rainProb: number;
  iconEmoji: string;
}

export interface AgriculturalWeather {
  locationName: string;
  currentTemp: number;
  feelsLike: number;
  humidity: number;
  windSpeed: number;
  rainfallChance: number;
  weatherCondition: string;
  weatherIcon: string;
  isRaining: boolean;
  sprayStatus: 'FAVORABLE' | 'CAUTION' | 'UNFAVORABLE';
  sprayReason: string;
  irrigationAdvice: string;
  diseaseRiskAlert: string;
  forecast: DailyForecast[];
}
