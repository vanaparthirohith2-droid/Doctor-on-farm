import { AppLanguage, CropSuggestion, DiseaseDiagnosis } from '../types';

export const APP_LANGUAGES: AppLanguage[] = [
  { code: 'hi', englishName: 'Hindi', nativeName: 'हिन्दी', flag: '🇮🇳', ttsVoiceLang: 'hi-IN' },
  { code: 'en', englishName: 'English', nativeName: 'English', flag: '🌐', ttsVoiceLang: 'en-US' },
  { code: 'pa', englishName: 'Punjabi', nativeName: 'ਪੰਜਾਬੀ', flag: '🇮🇳', ttsVoiceLang: 'pa-IN' },
  { code: 'te', englishName: 'Telugu', nativeName: 'తెలుగు', flag: '🇮🇳', ttsVoiceLang: 'te-IN' },
  { code: 'ta', englishName: 'Tamil', nativeName: 'தமிழ்', flag: '🇮🇳', ttsVoiceLang: 'ta-IN' },
  { code: 'bn', englishName: 'Bengali', nativeName: 'বাংলা', flag: '🇮🇳', ttsVoiceLang: 'bn-IN' },
  { code: 'mr', englishName: 'Marathi', nativeName: 'मराठी', flag: '🇮🇳', ttsVoiceLang: 'mr-IN' },
  { code: 'gu', englishName: 'Gujarati', nativeName: 'ગુજરાતી', flag: '🇮🇳', ttsVoiceLang: 'gu-IN' },
  { code: 'es', englishName: 'Spanish', nativeName: 'Español', flag: '🇪🇸', ttsVoiceLang: 'es-ES' },
  { code: 'fr', englishName: 'French', nativeName: 'Français', flag: '🇫🇷', ttsVoiceLang: 'fr-FR' },
  { code: 'sw', englishName: 'Swahili', nativeName: 'Kiswahili', flag: '🇰🇪', ttsVoiceLang: 'sw-KE' }
];

export interface SampleCropLeaf {
  id: string;
  nameEn: string;
  nameHi: string;
  diseaseEn: string;
  diseaseHi: string;
  color: string;
  bgColor: string;
  icon: string;
  description: string;
}

export const SAMPLE_LEAVES: SampleCropLeaf[] = [
  {
    id: 'tomato_early_blight',
    nameEn: 'Tomato',
    nameHi: 'टमाटर',
    diseaseEn: 'Early Blight (Alternaria solani)',
    diseaseHi: 'अगेती झुलसा (Early Blight)',
    color: '#DC2626',
    bgColor: '#FEE2E2',
    icon: '🍅',
    description: 'Concentric dark target rings on lower foliage'
  },
  {
    id: 'rice_blast',
    nameEn: 'Rice / Paddy',
    nameHi: 'धान / चावल',
    diseaseEn: 'Rice Blast (Magnaporthe oryzae)',
    diseaseHi: 'धान का झोंका रोग (Rice Blast)',
    color: '#16A34A',
    bgColor: '#DCFCE7',
    icon: '🌾',
    description: 'Spindle-shaped diamond spots with grey centers'
  },
  {
    id: 'wheat_yellow_rust',
    nameEn: 'Wheat',
    nameHi: 'गेहूं',
    diseaseEn: 'Yellow Stripe Rust (Puccinia striiformis)',
    diseaseHi: 'पीला रतुआ (Yellow Rust)',
    color: '#D97706',
    bgColor: '#FEF3C7',
    icon: '🌿',
    description: 'Yellow powdery pustules in parallel leaf stripes'
  },
  {
    id: 'potato_late_blight',
    nameEn: 'Potato',
    nameHi: 'आलू',
    diseaseEn: 'Late Blight (Phytophthora infestans)',
    diseaseHi: 'पछेती झुलसा (Late Blight)',
    color: '#9333EA',
    bgColor: '#F3E8FF',
    icon: '🥔',
    description: 'Water-soaked dark lesions with white mildew underside'
  },
  {
    id: 'cotton_leaf_curl',
    nameEn: 'Cotton',
    nameHi: 'कपास',
    diseaseEn: 'Cotton Leaf Curl Virus (CLCuV)',
    diseaseHi: 'कपास मरोड़िया रोग (Leaf Curl)',
    color: '#0284C7',
    bgColor: '#E0F2FE',
    icon: '🌱',
    description: 'Upward vein thickening and cup-shaped leaf curling'
  },
  {
    id: 'maize_fall_armyworm',
    nameEn: 'Maize / Corn',
    nameHi: 'मक्का',
    diseaseEn: 'Fall Armyworm (Spodoptera frugiperda)',
    diseaseHi: 'मक्का फॉल आर्मीवर्म',
    color: '#EA580C',
    bgColor: '#FFEDD5',
    icon: '🌽',
    description: 'Ragged feeding holes and coarse sawdust frass in whorl'
  },
  {
    id: 'chilli_leaf_curl',
    nameEn: 'Chilli / Pepper',
    nameHi: 'मिर्च',
    diseaseEn: 'Chilli Thrips & Leaf Curl',
    diseaseHi: 'मिर्च चुर्रा-मुर्रा रोग',
    color: '#E11D48',
    bgColor: '#FFE4E6',
    icon: '🌶️',
    description: 'Boat-shaped upward curling with stunted bushy growth'
  },
  {
    id: 'healthy_crop_leaf',
    nameEn: 'Healthy Green Leaf',
    nameHi: 'स्वस्थ फसल की पत्ती',
    diseaseEn: 'Healthy - No Disease Detected',
    diseaseHi: 'स्वस्थ - कोई रोग नहीं',
    color: '#059669',
    bgColor: '#D1FAE5',
    icon: '✨',
    description: 'Vibrant green chlorophyll, uniform lamina without lesions'
  }
];

let diagUniqueCounter = 0;
export function generateDiagId(prefix = 'diag'): string {
  diagUniqueCounter += 1;
  return `${prefix}_${Date.now()}_${Math.random().toString(36).substring(2, 9)}_${diagUniqueCounter}`;
}

export function getOfflineDiagnosis(sampleId: string, langCode: string): DiseaseDiagnosis {
  if (sampleId === 'rice_blast') {
    return {
      id: generateDiagId(sampleId),
      cropName: langCode === 'hi' ? 'धान / चावल (Paddy Rice)' : 'Paddy Rice (Oryza sativa)',
      cropScientificName: 'Oryza sativa',
      diseaseName: langCode === 'hi' ? 'धान का झोंका रोग (Rice Blast)' : 'Rice Blast (Magnaporthe oryzae)',
      scientificName: 'Magnaporthe oryzae',
      isHealthy: false,
      severityLevel: 'High',
      confidenceScore: 95,
      summary: langCode === 'hi'
        ? 'यह एक विनाशकारी फफूंद जनित रोग है। पत्तियों पर नाव अथवा आंख के आकार के धब्बे बनते हैं जिनका किनारा भूरा और केंद्र धूसर राख जैसा होता है।'
        : 'One of the most destructive rice fungal diseases forming diamond/spindle-shaped lesions with ash-grey centers and reddish-brown borders.',
      symptoms: [
        'Spindle / eye-shaped lesions with pointed ends on leaf blades',
        'Ash-grey centers with reddish-brown margins',
        'Neck blast: black rotting at base of panicles causing empty chaffy grains'
      ],
      causes: [
        'Excessive nitrogenous (Urea) fertilizer application',
        'Extended leaf moisture (> 10 hours) and cloudy overcast spells',
        'Dense planting density restricting airflow'
      ],
      organicTreatments: [
        langCode === 'hi'
          ? 'स्यूडोमोनास फ्लोरेसेंस (Pseudomonas fluorescens) 10 ग्राम प्रति लीटर पानी में मिलाकर पत्तियों पर छिड़कें।'
          : 'Foliar spray of Pseudomonas fluorescens bio-fungicide @ 10g / Litre of water.',
        langCode === 'hi'
          ? 'गोमूत्र एवं नीम अर्क (1:10 अनुपात) का 10 दिन के अंतराल पर स्प्रे करें।'
          : 'Fermented cow urine and neem leaf extract (1:10 dilution) every 10 days.',
        langCode === 'hi'
          ? 'खेत में यूरिया का प्रयोग तुरंत रोकें और पोटाश का संतुलन बनाएं।'
          : 'Immediately stop urea top-dressing and balance with potash.'
      ],
      chemicalTreatments: [
        {
          medicineName: 'Tricyclazole 75% WP (Beam / Baan)',
          dosage: '0.6 g / Litre (120 g / Acre in 200L water)',
          instructions: 'Apply systemic spray at symptom onset; ensures quick translaminar cure.',
          safetyWarning: 'Withhold application 21 days before harvest.'
        },
        {
          medicineName: 'Isoprothiolane 40% EC (Fuji-one)',
          dosage: '1.5 - 2.0 ml / Litre (300 ml / Acre)',
          instructions: 'Protects both leaf blades and emerging panicle neck.',
          safetyWarning: 'Maintain 2-3 cm standing water in field during spray.'
        }
      ],
      preventiveMeasures: [
        'Seed treatment with Carbendazim (2g/kg) before nursery sowing',
        'Adopt blast-tolerant varieties (Swarna Sub-1, IR64)',
        'Maintain split application of potash to strengthen leaf silica layer'
      ],
      sprayWindowRecommendation: 'Spray during morning calm hours (7 AM - 10 AM) with fine mist nozzle.',
      languageCode: langCode,
      timestamp: Date.now()
    };
  }

  if (sampleId === 'wheat_yellow_rust') {
    return {
      id: generateDiagId(sampleId),
      cropName: langCode === 'hi' ? 'गेहूं (Wheat)' : 'Wheat (Triticum aestivum)',
      cropScientificName: 'Triticum aestivum',
      diseaseName: langCode === 'hi' ? 'पीला रतुआ / हल्दी रोग (Stripe Rust)' : 'Yellow Stripe Rust (Puccinia striiformis)',
      scientificName: 'Puccinia striiformis',
      isHealthy: false,
      severityLevel: 'Severe',
      confidenceScore: 97,
      summary: langCode === 'hi'
        ? 'यह पक्सीनिया स्ट्राइफॉर्मिस फफूंद से तेजी से फैलने वाला रोग है। पत्तियों पर पीले रंग की पाउडर जैसी समानांतर धारियां बन जाती हैं जो हाथ पर हल्दी की तरह लगती हैं।'
        : 'An aggressive airborne fungal rust creating bright yellow-orange powdery pustules in parallel linear stripes along leaf veins.',
      symptoms: [
        'Bright yellow-orange linear pustules arranged in parallel stripes along leaf veins',
        'Yellow powdery spores rub off easily on hands or clothes',
        'Premature drying of flag leaves causing shrivelled kernels'
      ],
      causes: [
        'Cool temperatures (10-18°C) accompanied by heavy morning fog or dew',
        'Airborne spores blown by winds from northern hills'
      ],
      organicTreatments: [
        langCode === 'hi'
          ? 'खट्टी छाछ (5-6 दिन पुरानी छाछ 5 लीटर प्रति एकड़) का 100 लीटर पानी में छिड़काव करें।'
          : 'Sour buttermilk spray (5 Litres in 100L water per acre) to lower leaf surface pH.',
        langCode === 'hi'
          ? 'गोमूत्र व हींग का काढ़ा (20 ग्राम हींग + 5 लीटर गोमूत्र / 100 लीटर पानी)।'
          : 'Asafoetida (Hing 20g) + fermented cow urine decoction.'
      ],
      chemicalTreatments: [
        {
          medicineName: 'Propiconazole 25% EC (Tilt)',
          dosage: '1.0 ml / Litre (200 ml in 200 Litres water / Acre)',
          instructions: 'Direct systemic foliar spray immediately upon seeing first yellow stripe.',
          safetyWarning: 'Wear protective face mask and gloves. Highly toxic to aquatic organisms.'
        },
        {
          medicineName: 'Tebuconazole 25.9% EC (Folicur)',
          dosage: '1.0 - 1.2 ml / Litre',
          instructions: 'Curative action stopping rust sporulation within 48 hours.',
          safetyWarning: 'Do not spray in extreme direct afternoon sunlight.'
        }
      ],
      preventiveMeasures: [
        'Plant rust-resistant certified varieties (DBW 187, DBW 222, HD 3226, PBW 725)',
        'Regular scouting on field borders and shady tree patches',
        'Avoid late sowing in sub-mountainous zones'
      ],
      sprayWindowRecommendation: 'Spray immediately once morning fog lifts on a sunny calm day.',
      languageCode: langCode,
      timestamp: Date.now()
    };
  }

  if (sampleId === 'healthy_crop_leaf') {
    return {
      id: generateDiagId(sampleId),
      cropName: langCode === 'hi' ? 'स्वस्थ फसल (Healthy Crop Leaf)' : 'Healthy Plant Leaf',
      cropScientificName: 'Plantae',
      diseaseName: langCode === 'hi' ? 'कोई रोग नहीं मिला (Healthy Crop)' : 'Healthy - No Pathogens Detected',
      scientificName: 'Healthy',
      isHealthy: true,
      severityLevel: 'Healthy',
      confidenceScore: 99,
      summary: langCode === 'hi'
        ? 'बधाई हो! आपकी फसल की पत्तियां पूर्णतः स्वस्थ हैं। पत्तियों का गहरा हरा रंग, सामान्य शिरा विन्यास और बनावट आदर्श है। कोई कीट या फफूंद नहीं मिला।'
        : 'Great news! The plant foliage is vigorous, dark green, and completely free from detectable lesions, spots, rusts, or pest infestations.',
      symptoms: [
        'Uniform green chlorophyll pigmentation',
        'Intact leaf margin without lesions or necrotic halos',
        'Active vegetative growth'
      ],
      causes: [],
      organicTreatments: [
        langCode === 'hi'
          ? 'फसल की प्राकृतिक प्रतिरोधक क्षमता बनाए रखने हेतु 15 दिन में जीवामृत का प्रयोग करें।'
          : 'Apply Jeevamrutha or fermented bio-extract every 15 days to nurture plant vitality.',
        langCode === 'hi'
          ? 'संतुलित ड्रिप सिंचाई व खरपतवार नियंत्रण जारी रखें।'
          : 'Maintain balanced drip irrigation and clean field bunds.'
      ],
      chemicalTreatments: [],
      preventiveMeasures: [
        'Continue weekly crop scouting to detect any sudden infestations early',
        'Maintain balanced N-P-K nutrition with secondary micronutrients (Zinc, Boron)',
        'Ensure proper drainage to prevent waterlogging'
      ],
      sprayWindowRecommendation: 'No chemical spray required. Continue routine care.',
      languageCode: langCode,
      timestamp: Date.now()
    };
  }

  // Default Tomato Early Blight
  return {
    id: generateDiagId('tomato_early_blight'),
    cropName: langCode === 'hi' ? 'टमाटर (Tomato)' : 'Tomato (Solanum lycopersicum)',
    cropScientificName: 'Solanum lycopersicum',
    diseaseName: langCode === 'hi' ? 'अगेती झुलसा रोग (Early Blight)' : 'Early Blight (Alternaria solani)',
    scientificName: 'Alternaria solani',
    isHealthy: false,
    severityLevel: 'Moderate',
    confidenceScore: 96,
    summary: langCode === 'hi'
      ? 'यह एक फफूंद जनित रोग (Alternaria solani) है। निचली पत्तियों पर भूरे-काले छल्लेदार धब्बे बनते हैं और पत्तियां पीली पड़कर सूखने लगती हैं।'
      : 'A fungal disease causing dark concentric target-board spots on older leaves, spreading upwards and leading to defoliation.',
    symptoms: [
      'Bullseye concentric rings on older lower leaves',
      'Yellow chlorotic halo around leaf spots',
      'Premature defoliation exposing green fruits to sunscald',
      'Stem cankers with sunken dark lesions'
    ],
    causes: [
      'High relative humidity (> 80%) coupled with warm temperatures (24-29°C)',
      'Splashing rain droplets from contaminated soil onto lower foliage',
      'Overcrowded plant spacing restricting airflow'
    ],
    organicTreatments: [
      langCode === 'hi'
        ? 'नीम तेल (10,000 ppm) 3-4 मिली प्रति लीटर पानी में मिलाकर 7 दिन के अंतराल पर छिड़कें।'
        : 'Spray Neem Seed Oil (10,000 ppm) at 3-4 ml / Litre water every 7 days.',
      langCode === 'hi'
        ? 'ट्राइकोडर्मा विरिडी (Trichoderma viride) 5 ग्राम प्रति लीटर पानी में मिलाकर मिट्टी और पत्तियों पर स्प्रे करें।'
        : 'Apply Trichoderma viride biological bio-fungicide @ 5g / Litre.',
      langCode === 'hi'
        ? 'संक्रमित निचली पत्तियों को तुरंत काटकर खेत से दूर नष्ट कर दें।'
        : 'Prune and safely discard lower infected leaves to stop spore splash.'
    ],
    chemicalTreatments: [
      {
        medicineName: 'Copper Oxychloride 50% WP (Blitox)',
        dosage: '2.5 - 3.0 g / Litre (500 g / Acre)',
        instructions: 'Spray thoroughly on both upper and lower leaf surfaces during early morning.',
        safetyWarning: 'Do not spray during peak afternoon sun. Wear rubber gloves and face mask.'
      },
      {
        medicineName: 'Mancozeb 75% WP (Dithane M-45)',
        dosage: '2.0 - 2.5 g / Litre (400 - 500 g / Acre)',
        instructions: 'Apply at 10-12 days interval if disease pressure persists.',
        safetyWarning: 'Wait at least 7 days after application before harvesting fruits.'
      },
      {
        medicineName: 'Azoxystrobin 18.2% + Difenoconazole 11.4% SC',
        dosage: '1.0 ml / Litre (200 ml / Acre)',
        instructions: 'Dual systemic action for fast curable control in moderate to severe outbreak.',
        safetyWarning: 'Rotate chemistries to prevent fungicide resistance.'
      }
    ],
    preventiveMeasures: [
      'Use drip irrigation instead of overhead sprinklers to keep foliage dry',
      'Apply straw or black plastic mulch around tomato beds to prevent soil splashing',
      'Follow 3-year crop rotation avoiding Solanaceae family (potato, brinjal, chilli)'
    ],
    sprayWindowRecommendation: 'Best spray window: 6:30 AM - 9:30 AM when wind speed is under 10 km/h.',
    languageCode: langCode,
    timestamp: Date.now()
  };
}

export const SEASONAL_CROPS: CropSuggestion[] = [
  // KHARIF
  {
    id: 'rice_paddy',
    nameEn: 'Paddy / Rice',
    nameHi: 'धान / चावल',
    category: 'Cereals',
    season: 'KHARIF',
    seasonLabelHi: 'खरीफ (मानसून)',
    sowingMonths: 'June - July',
    harvestingDurationDays: 120,
    soilTypes: ['Clay', 'Clay Loam', 'Alluvial'],
    waterRequirement: 'High (Standing water / Assured irrigation)',
    estimatedYield: '20 - 28 Quintals / Acre',
    profitPotential: 'High',
    seedRate: '8 - 10 kg / Acre (Transplanted)',
    spacing: '20 cm x 15 cm',
    keyTips: 'Transplant 21-25 day seedlings; maintain 3-5 cm water layer until grain milk stage.',
    companionCrops: ['Azolla bio-fertilizer', 'Fish-paddy farming', 'Sesbania green manure'],
    pestResistantVarieties: ['Pusa Basmati 1509', 'PR 126', 'Swarna Sub-1', 'MTU 1010']
  },
  {
    id: 'cotton_cash',
    nameEn: 'Cotton (Kapas)',
    nameHi: 'कपास (नरमा)',
    category: 'Cash Crop',
    season: 'KHARIF',
    seasonLabelHi: 'खरीफ (मानसून)',
    sowingMonths: 'May - June',
    harvestingDurationDays: 160,
    soilTypes: ['Black Cotton Soil', 'Deep Alluvial Loam'],
    waterRequirement: 'Moderate (Drip irrigation ideal)',
    estimatedYield: '10 - 15 Quintals / Acre',
    profitPotential: 'Very High',
    seedRate: '1.5 - 2.0 Packets (Bt) / Acre',
    spacing: '90 cm x 60 cm',
    keyTips: 'Deep summer ploughing; install pheromone traps for pink bollworm by day 45.',
    companionCrops: ['Cowpea', 'Maize border barrier', 'Greengram'],
    pestResistantVarieties: ['RCH 659 BG II', 'Bhakti BG II', 'First Class BG II']
  },
  {
    id: 'soybean_oil',
    nameEn: 'Soybean',
    nameHi: 'सोयाबीन',
    category: 'Oilseeds & Pulses',
    season: 'KHARIF',
    seasonLabelHi: 'खरीफ (मानसून)',
    sowingMonths: 'June - July',
    harvestingDurationDays: 95,
    soilTypes: ['Black Soil', 'Loamy Soil'],
    waterRequirement: 'Rainfed to Moderate',
    estimatedYield: '10 - 14 Quintals / Acre',
    profitPotential: 'High',
    seedRate: '25 - 30 kg / Acre',
    spacing: '45 cm x 10 cm',
    keyTips: 'Treat seeds with Rhizobium culture + Trichoderma before sowing. Naturally fixes nitrogen.',
    companionCrops: ['Maize', 'Pigeon pea (4:2 intercrop)'],
    pestResistantVarieties: ['JS 20-34', 'JS 95-60', 'NRC 37', 'RVS 2001-4']
  },
  {
    id: 'maize_corn',
    nameEn: 'Maize (Corn)',
    nameHi: 'मक्का (भुट्टा)',
    category: 'Cereals & Fodder',
    season: 'KHARIF',
    seasonLabelHi: 'खरीफ (मानसून)',
    sowingMonths: 'June - July',
    harvestingDurationDays: 100,
    soilTypes: ['Sandy Loam', 'Alluvial'],
    waterRequirement: 'Moderate',
    estimatedYield: '25 - 35 Quintals / Acre',
    profitPotential: 'High',
    seedRate: '7 - 8 kg / Acre',
    spacing: '60 cm x 20 cm',
    keyTips: 'Do not let water stagnate; scout for fall armyworm in early 2-4 leaf whorl stage.',
    companionCrops: ['Cowpea', 'Beans', 'Pumpkin'],
    pestResistantVarieties: ['DKC 9108', 'Pioneer P3396', 'HQPM 1', 'Ganga 11']
  },

  // RABI
  {
    id: 'wheat_staple',
    nameEn: 'Wheat (Gehun)',
    nameHi: 'गेहूं',
    category: 'Cereals',
    season: 'RABI',
    seasonLabelHi: 'रबी (सर्दियां)',
    sowingMonths: 'November - December',
    harvestingDurationDays: 135,
    soilTypes: ['Clay Loam', 'Alluvial', 'Loamy Soil'],
    waterRequirement: 'Moderate (4-5 critical irrigations)',
    estimatedYield: '22 - 28 Quintals / Acre',
    profitPotential: 'High',
    seedRate: '40 - 45 kg / Acre',
    spacing: '20 cm row-to-row',
    keyTips: 'First irrigation at CRI stage (21 days) is vital; avoid sowing in high early temperatures.',
    companionCrops: ['Mustard (9:1 ratio)', 'Chickpea'],
    pestResistantVarieties: ['DBW 187 (Karan Vandana)', 'DBW 222', 'HD 3086', 'HD 3226']
  },
  {
    id: 'mustard_sarson',
    nameEn: 'Mustard (Sarson / Rai)',
    nameHi: 'सरसों / राई',
    category: 'Oilseeds',
    season: 'RABI',
    seasonLabelHi: 'रबी (सर्दियां)',
    sowingMonths: 'October - November',
    harvestingDurationDays: 115,
    soilTypes: ['Sandy Loam', 'Alluvial', 'Light Soil'],
    waterRequirement: 'Low (1-2 irrigations)',
    estimatedYield: '8 - 12 Quintals / Acre',
    profitPotential: 'Very High',
    seedRate: '1.5 - 2.0 kg / Acre',
    spacing: '30 cm x 10 cm',
    keyTips: 'Apply Sulphur @ 10 kg/Acre to boost edible oil percentage by 2-3%.',
    companionCrops: ['Wheat', 'Gram', 'Potato border'],
    pestResistantVarieties: ['Pusa Bold', 'Giriraj', 'RH 749', 'NRCHB 101']
  },
  {
    id: 'chickpea_chana',
    nameEn: 'Chickpea / Bengal Gram (Chana)',
    nameHi: 'चना (देसी व काबुली)',
    category: 'Pulses',
    season: 'RABI',
    seasonLabelHi: 'रबी (सर्दियां)',
    sowingMonths: 'October - November',
    harvestingDurationDays: 110,
    soilTypes: ['Loam', 'Black Soil'],
    waterRequirement: 'Low (Thrives on residual moisture)',
    estimatedYield: '8 - 14 Quintals / Acre',
    profitPotential: 'Very High',
    seedRate: '30 - 35 kg / Acre',
    spacing: '30 cm x 10 cm',
    keyTips: 'Pinch shoot tips at 30-35 days to promote heavy branching and pod setting.',
    companionCrops: ['Mustard', 'Barley', 'Linseed'],
    pestResistantVarieties: ['JG 11', 'JAKI 9218', 'Pusa 362', 'Super Chana']
  },
  {
    id: 'potato_vegetable',
    nameEn: 'Potato (Aloo)',
    nameHi: 'आलू',
    category: 'Vegetables & Tubers',
    season: 'RABI',
    seasonLabelHi: 'रबी (सर्दियां)',
    sowingMonths: 'October - November',
    harvestingDurationDays: 90,
    soilTypes: ['Sandy Loam', 'Friable Alluvial'],
    waterRequirement: 'Moderate to High',
    estimatedYield: '100 - 140 Quintals / Acre',
    profitPotential: 'Very High',
    seedRate: '10 - 12 Quintals tubers / Acre',
    spacing: '60 cm x 20 cm',
    keyTips: 'Earthing up at 30 days is essential to avoid greening of tubers from direct sunlight.',
    companionCrops: ['Mustard border', 'Marigold for nematodes'],
    pestResistantVarieties: ['Kufri Pukhraj', 'Kufri Jyoti', 'Kufri Mohan', 'Kufri Chipsona']
  },

  // ZAID (SUMMER)
  {
    id: 'watermelon_tarbooj',
    nameEn: 'Watermelon (Tarbooj)',
    nameHi: 'तरबूज',
    category: 'Cucurbits & Fruits',
    season: 'ZAID',
    seasonLabelHi: 'जायद (गर्मी)',
    sowingMonths: 'February - March',
    harvestingDurationDays: 75,
    soilTypes: ['Sandy Loam', 'River Sandy Beds'],
    waterRequirement: 'Moderate (Drip + Mulch ideal)',
    estimatedYield: '150 - 220 Quintals / Acre',
    profitPotential: 'Very High',
    seedRate: '400 - 500 g hybrid seeds / Acre',
    spacing: '2.0 m bed x 45 cm plant',
    keyTips: 'Use 25-micron silver-black plastic mulch and drip fertigation for high brix sweet fruit.',
    companionCrops: ['Muskmelon', 'Cucumber'],
    pestResistantVarieties: ['Maxx (Syngenta)', 'Kundan', 'Black Suger Baby', 'Pusa Bedana']
  },
  {
    id: 'cucumber_kheera',
    nameEn: 'Cucumber (Kheera)',
    nameHi: 'खीरा',
    category: 'Vegetables',
    season: 'ZAID',
    seasonLabelHi: 'जायद (गर्मी)',
    sowingMonths: 'February - April',
    harvestingDurationDays: 55,
    soilTypes: ['Sandy Loam', 'Organic Rich Loam'],
    waterRequirement: 'Moderate (Drip irrigation)',
    estimatedYield: '60 - 90 Quintals / Acre',
    profitPotential: 'Very High',
    seedRate: '300 - 400 g / Acre',
    spacing: '1.5 m x 30 cm',
    keyTips: 'Grow on vertical trellis or net staking for straight, spot-free premium green fruits.',
    companionCrops: ['Sunflower', 'Radish', 'Bush beans'],
    pestResistantVarieties: ['Malini', 'Kian', 'Pusa Uday', 'Gypsy F1']
  },
  {
    id: 'green_gram_moong',
    nameEn: 'Summer Moong (Green Gram)',
    nameHi: 'ग्रीष्मकालीन मूंग',
    category: 'Pulses',
    season: 'ZAID',
    seasonLabelHi: 'जायद (गर्मी)',
    sowingMonths: 'March - April',
    harvestingDurationDays: 60,
    soilTypes: ['Loamy', 'Alluvial'],
    waterRequirement: 'Low (3 light irrigations)',
    estimatedYield: '5 - 7 Quintals / Acre',
    profitPotential: 'High',
    seedRate: '10 - 12 kg / Acre',
    spacing: '22.5 cm x 10 cm',
    keyTips: 'Short 60-day crop fixes 35-40 kg atmospheric nitrogen per hectare into soil for next paddy.',
    companionCrops: ['Sugarcane intercrop', 'Spring maize'],
    pestResistantVarieties: ['Pusa Vishal', 'SML 668', 'IPM 205-7 (Virat)', 'Samrat']
  }
];
