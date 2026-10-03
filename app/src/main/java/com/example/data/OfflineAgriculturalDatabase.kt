package com.example.data

import com.example.model.AppLanguage
import com.example.model.DiseaseDiagnosis
import com.example.model.TreatmentDetail

object OfflineAgriculturalDatabase {

    fun getDiagnosisForSample(sampleId: String, lang: AppLanguage): DiseaseDiagnosis {
        return when (sampleId) {
            "tomato_early_blight" -> getTomatoEarlyBlight(lang)
            "rice_blast" -> getRiceBlast(lang)
            "wheat_yellow_rust" -> getWheatYellowRust(lang)
            "potato_late_blight" -> getPotatoLateBlight(lang)
            "cotton_leaf_curl" -> getCottonLeafCurl(lang)
            "maize_fall_armyworm" -> getMaizeArmyworm(lang)
            "chilli_leaf_curl" -> getChilliLeafCurl(lang)
            "healthy_crop_leaf" -> getHealthyLeaf(lang)
            else -> getTomatoEarlyBlight(lang)
        }
    }

    private fun getTomatoEarlyBlight(lang: AppLanguage): DiseaseDiagnosis {
        val (cropName, diseaseName, summary) = when (lang) {
            AppLanguage.HINDI -> Triple(
                "टमाटर (Tomato)",
                "अगेती झुलसा रोग (Early Blight)",
                "यह एक फफूंद जनित रोग है (Alternaria solani)। निचली पत्तियों पर भूरे-काले छल्लेदार धब्बे बनते हैं और पत्तियां पीली पड़कर सूखने लगती हैं।"
            )
            AppLanguage.PUNJABI -> Triple(
                "ਟਮਾਟਰ (Tomato)",
                "ਅਗੇਤੀ ਝੁਲਸਾ ਰੋਗ (Early Blight)",
                "ਇਹ ਉੱਲੀ ਰੋਗ (Alternaria solani) ਹੈ। ਹੇਠਲੇ ਪੱਤਿਆਂ ਤੇ ਗੋਲ ਭੂਰੇ ਧੱਬੇ ਬਣਦੇ ਹਨ ਅਤੇ ਪੱਤੇ ਸੁੱਕਣ ਲੱਗਦੇ ਹਨ।"
            )
            AppLanguage.TELUGU -> Triple(
                "టమోటా (Tomato)",
                "ముందస్తు మాడు తెగులు (Early Blight)",
                "ఇది ఆల్టర్నేరియా సొలాని వల్ల కలిగే శిలీంధ్ర వ్యాధి. ఆకులపై నల్లటి వలయాలు ఏర్పడి ఎండిపోతాయి."
            )
            AppLanguage.TAMIL -> Triple(
                "தக்காளி (Tomato)",
                "முன் பருவ கருகல் நோய் (Early Blight)",
                "ஆல்டர்நேரியா சோலானி பூஞ்சையால் ஏற்படும் நோய். இலைகளில் வட்டமான கரும் புள்ளிகள் தோன்றும்."
            )
            AppLanguage.BENGALI -> Triple(
                "টমেটো (Tomato)",
                "আগাম ধসা রোগ (Early Blight)",
                "অল্টারনারিয়া সোলানি ছত্রাকের কারণে পাতার উপর গাঢ় বাদামী বৃত্তাকার দাগ পড়ে ও পাতা শুকিয়ে যায়।"
            )
            AppLanguage.MARATHI -> Triple(
                "टोमॅटो (Tomato)",
                "लवकर येणारा करपा (Early Blight)",
                "हा बुरशीजन्य रोग असून खालच्या पानांवर गडद तपकिरी वर्तुळाकार ठिपके पडतात आणि पाने सुकतात."
            )
            AppLanguage.GUJARATI -> Triple(
                "ટમેટા (Tomato)",
                "અગેતી સુકારો (Early Blight)",
                "આ અલ્ટરનેરિયા સોલાની ફૂગ દ્વારા ફેલાય છે. પાંદડા પર ગોળાકાર કાળા ડાઘ પડે છે."
            )
            AppLanguage.SPANISH -> Triple(
                "Tomate (Tomato)",
                "Tizón Temprano (Alternaria solani)",
                "Enfermedad fúngica caracterizada por manchas concéntricas de color marrón oscuro en hojas inferiores."
            )
            AppLanguage.FRENCH -> Triple(
                "Tomate (Tomato)",
                "Alternariose (Early Blight)",
                "Maladie fongique provoquant des taches brunes concentriques sur les feuilles inférieures."
            )
            AppLanguage.SWAHILI -> Triple(
                "Nyanya (Tomato)",
                "Ukungu wa Mapema (Early Blight)",
                "Ugonjwa wa fangasi unaosababisha madoa ya mviringo ya hudhurungi kwenye majani ya chini."
            )
            else -> Triple(
                "Tomato (Solanum lycopersicum)",
                "Early Blight (Alternaria solani)",
                "A fungal disease causing dark concentric target-board spots on older leaves, spreading upwards and reducing yield."
            )
        }

        val organic = when (lang) {
            AppLanguage.HINDI -> listOf(
                "नीम तेल (10,000 ppm) 3-4 मिली प्रति लीटर पानी में मिलाकर 7 दिन के अंतराल पर छिड़कें।",
                "ट्राइकोडर्मा विरिडी (Trichoderma viride) 5 ग्राम प्रति लीटर पानी में मिलाकर मिट्टी और पत्तियों पर स्प्रे करें।",
                "संक्रमित निचली पत्तियों को तुरंत काटकर खेत से दूर नष्ट कर दें।"
            )
            AppLanguage.PUNJABI -> listOf(
                "ਨਿੰਮ ਦਾ ਤੇਲ 3-4 ਮਿ.ਲੀ ਪ੍ਰਤੀ ਲੀਟਰ ਪਾਣੀ ਵਿੱਚ ਮਿਲਾ ਕੇ ਛਿੜਕਾਅ ਕਰੋ।",
                "ਟ੍ਰਾਈਕੋਡਰਮਾ 5 ਗ੍ਰਾਮ ਪ੍ਰਤੀ ਲੀਟਰ ਪਾਣੀ ਵਿੱਚ ਮਿਲਾ ਕੇ ਸਪਰੇਅ ਕਰੋ।",
                "ਰੋਗੀ ਪੱਤੇ ਤੁਰੰਤ ਤੋੜ ਕੇ ਸਾੜ ਦਿਓ।"
            )
            AppLanguage.TELUGU -> listOf(
                "వేప నూనె 3-4 మి.లీ లీటరు నీటిలో కలిపి పిచికారీ చేయండి.",
                "ట్రైకోడెర్మా విరిడే 5 గ్రాములు లీటరు నీటిలో పిచికారీ చేయండి.",
                "తెగులు సోకిన ఆకులను తీసివేయండి."
            )
            AppLanguage.MARATHI -> listOf(
                "निंबोळी अर्क (Neem oil) ३-४ मिली प्रति लिटर पाण्यात मिसळून फवारा.",
                "ट्रायकोडर्मा विरिडी ५ ग्रॅम प्रति लिटर फवारणी करा.",
                "रोगट पाने काढून नष्ट करा."
            )
            else -> listOf(
                "Spray Neem Seed Oil (10,000 ppm) at 3-4 ml/liter water every 7 days.",
                "Apply Trichoderma viride biological bio-fungicide @ 5g/liter.",
                "Prune and safely burn lower infected leaves to stop spore splash."
            )
        }

        val chemical = listOf(
            TreatmentDetail(
                medicineName = if (lang == AppLanguage.HINDI) "कॉपर ऑक्सीक्लोराइड (Copper Oxychloride 50% WP)" else "Copper Oxychloride 50% WP",
                dosage = "2.5 - 3.0 g / Litre (500 g / Acre)",
                instructions = "Spray thoroughly on both upper and lower leaf surfaces during early morning.",
                safetyWarning = "Do not spray during peak afternoon sun. Wear rubber gloves and face mask."
            ),
            TreatmentDetail(
                medicineName = if (lang == AppLanguage.HINDI) "मैंकोजेब 75% WP (Mancozeb)" else "Mancozeb 75% WP (Dithane M-45)",
                dosage = "2.0 - 2.5 g / Litre (400 - 500 g / Acre)",
                instructions = "Apply at 10-12 days interval if disease pressure persists.",
                safetyWarning = "Wait at least 7 days after application before harvesting fruits."
            ),
            TreatmentDetail(
                medicineName = if (lang == AppLanguage.HINDI) "एज़ोक्सीस्ट्रोबिन + डिफेनोकोनाज़ोल" else "Azoxystrobin 18.2% + Difenoconazole 11.4% SC",
                dosage = "1 ml / Litre (200 ml / Acre)",
                instructions = "Systemic action for fast curable control in moderate to severe outbreak.",
                safetyWarning = "Rotate chemistries to prevent fungicide resistance."
            )
        )

        return DiseaseDiagnosis(
            cropName = cropName,
            cropScientificName = "Solanum lycopersicum",
            diseaseName = diseaseName,
            scientificName = "Alternaria solani",
            isHealthy = false,
            severityLevel = "Moderate",
            confidenceScore = 96,
            summary = summary,
            symptoms = listOf(
                "Bullseye concentric rings on older lower leaves",
                "Yellow chlorotic halo around leaf spots",
                "Premature defoliation exposing green fruits to sunscald",
                "Stem cankers with sunken dark lesions"
            ),
            causes = listOf(
                "High relative humidity (> 80%) coupled with warm temperatures (24-29°C)",
                "Splashing rain droplets from contaminated soil onto lower foliage",
                "Overcrowded plant spacing restricting airflow"
            ),
            organicTreatments = organic,
            chemicalTreatments = chemical,
            preventiveMeasures = listOf(
                "Drip irrigation instead of overhead sprinklers to keep foliage dry",
                "Apply paddy straw or black plastic mulch around tomato beds",
                "Follow 3-year crop rotation avoiding Solanaceae family (potato, brinjal, chilli)"
            ),
            sprayWindowRecommendation = "Best spray window: 6:30 AM - 9:30 AM when wind speed is under 10 km/h.",
            languageCode = lang.code
        )
    }

    private fun getRiceBlast(lang: AppLanguage): DiseaseDiagnosis {
        val (cropName, diseaseName, summary) = when (lang) {
            AppLanguage.HINDI -> Triple(
                "धान / चावल (Paddy Rice)",
                "धान का झोंका रोग (Rice Blast)",
                "यह मैगनापोर्थे ओराइजी (Magnaporthe oryzae) फफूंद से होता है। पत्तियों पर नाव के आकार के धब्बे बनते हैं जिनका किनारा भूरा और केंद्र राख जैसा होता है।"
            )
            AppLanguage.PUNJABI -> Triple(
                "ਝੋਨਾ / ਧਾਨ (Paddy Rice)",
                "ਝੋਨੇ ਦਾ ਬਲਾਸਟ / ਝੁਲਸਾ ਰੋਗ (Rice Blast)",
                "ਇਹ ਖਤਰਨਾਕ ਉੱਲੀ ਰੋਗ ਹੈ। ਪੱਤਿਆਂ 'ਤੇ ਤੱਕਲੇ ਵਰਗੇ ਧੱਬੇ ਬਣਦੇ ਹਨ ਜਿਸ ਨਾਲ ਪੈਦਾਵਾਰ ਘਟਦੀ ਹੈ।"
            )
            AppLanguage.TELUGU -> Triple(
                "వరి (Paddy Rice)",
                "అగ్గితెగులు (Rice Blast)",
                "ఇది అత్యంత హానికరమైన శిలీంధ్ర తెగులు. ఆకులపై కంటి ఆకారంలో మచ్చలు ఏర్పడతాయి."
            )
            AppLanguage.TAMIL -> Triple(
                "நெல் (Paddy Rice)",
                "நெல் குலை நோய் (Rice Blast)",
                "இலைகளில் கண் வடிவ புள்ளிகள் தோன்றி முழுப் பயிரையும் பாதிக்கும் பூஞ்சை நோய்."
            )
            AppLanguage.BENGALI -> Triple(
                "ধান (Paddy Rice)",
                "ধানের ব্লাস্ট রোগ (Rice Blast)",
                "পাতায় চোখের মতো বা নৌকার মতো ধূসর দাগ পড়ে, শীষ মরে যায়।"
            )
            AppLanguage.MARATHI -> Triple(
                "भात / धान (Paddy Rice)",
                "भातावरील करपा (Rice Blast)",
                "हा बुरशीजन्य रोग असून पानांवर डोळ्याच्या आकाराचे करडे ठिपके पडतात."
            )
            AppLanguage.GUJARATI -> Triple(
                "ડાંગર (Paddy Rice)",
                "ડાંગરનો ગેરુ / બ્લાસ્ટ (Rice Blast)",
                "પાંદડા પર આંખ જેવા આકારના રાખોડી ડાઘ પડે છે."
            )
            AppLanguage.SPANISH -> Triple(
                "Arroz (Paddy Rice)",
                "Piricularia del Arroz (Magnaporthe oryzae)",
                "Enfermedad destructiva que forma lesiones fusiformes con centros grises y márgenes marrones."
            )
            AppLanguage.FRENCH -> Triple(
                "Riz (Paddy Rice)",
                "Pyriculariose du riz (Rice Blast)",
                "Lésions en forme de fuseau avec centre grisâtre et pourtour brun sur le feuillage et le collet."
            )
            AppLanguage.SWAHILI -> Triple(
                "Mchele (Paddy Rice)",
                "Kipindupindu cha Mpunga (Rice Blast)",
                "Ugonjwa wa fangasi unaounda madoa ya umbo la almasi kwenye majani."
            )
            else -> Triple(
                "Paddy Rice (Oryza sativa)",
                "Rice Blast (Magnaporthe oryzae)",
                "One of the most destructive rice fungal diseases forming spindle-shaped lesions with ash-grey centers and reddish-brown borders."
            )
        }

        val organic = when (lang) {
            AppLanguage.HINDI -> listOf(
                "स्यूडोमोनास फ्लोरेसेंस (Pseudomonas fluorescens) 10 ग्राम प्रति लीटर पानी में मिलाकर छिड़कें।",
                "गोमूत्र एवं नीम पत्ती अर्क (1:10 अनुपात) का 10 दिन के अंतर पर स्प्रे करें।",
                "खेत में अत्यधिक यूरिया (नाइट्रोजन) का प्रयोग तुरंत रोकें।"
            )
            AppLanguage.PUNJABI -> listOf(
                "ਸਿਊਡੋਮੋਨਾਸ ਫਲੋਰੋਸੈਂਸ 10 ਗ੍ਰਾਮ ਪ੍ਰਤੀ ਲੀਟਰ ਪਾਣੀ ਨਾਲ ਛਿੜਕਾਅ ਕਰੋ।",
                "ਯੂਰੀਆ ਖਾਦ ਦੀ ਜ਼ਿਆਦਾ ਵਰਤੋਂ ਬੰਦ ਕਰੋ।",
                "ਖੇਤ ਵਿੱਚ ਪਾਣੀ ਦਾ ਸਹੀ ਨਿਕਾਸ ਰੱਖੋ।"
            )
            else -> listOf(
                "Foliar spray of Pseudomonas fluorescens @ 10g / liter water.",
                "Apply fermented cow urine and neem leaf extract (1:10 dilution).",
                "Immediately withhold excessive nitrogenous (Urea) top-dressing."
            )
        }

        val chemical = listOf(
            TreatmentDetail(
                medicineName = if (lang == AppLanguage.HINDI) "ट्राइसाइक्लाजोल 75% WP (Tricyclazole)" else "Tricyclazole 75% WP (Beam / Baan)",
                dosage = "0.6 g / Litre (120 g / Acre)",
                instructions = "Best systemic curative fungicide for leaf blast and neck blast. Spray immediately at symptom onset.",
                safetyWarning = "Do not apply within 21 days before grain harvest."
            ),
            TreatmentDetail(
                medicineName = if (lang == AppLanguage.HINDI) "आइसोप्रोपियोलेन 40% EC (Isoprothiolane)" else "Isoprothiolane 40% EC (Fuji-one)",
                dosage = "1.5 - 2.0 ml / Litre (300 ml / Acre)",
                instructions = "Provides systemic protection to both leaf blades and panicles.",
                safetyWarning = "Maintain a shallow standing water depth of 2-3 cm during spray."
            )
        )

        return DiseaseDiagnosis(
            cropName = cropName,
            cropScientificName = "Oryza sativa",
            diseaseName = diseaseName,
            scientificName = "Magnaporthe oryzae",
            isHealthy = false,
            severityLevel = "High",
            confidenceScore = 95,
            summary = summary,
            symptoms = listOf(
                "Diamond / eye-shaped spots with pointed ends on leaves",
                "Grey necrotic centers with dark brown borders",
                "Blackish rotting around panicle base causing empty whiteheads (neck blast)",
                "Complete wilting of leaf blades during severe epidemic"
            ),
            causes = listOf(
                "Excessive nitrogen fertilizer application causing lush succulent growth",
                "Extended leaf wetness (> 10 hours) and heavy dews or overcast days",
                "Dense planting density without proper alleyways"
            ),
            organicTreatments = organic,
            chemicalTreatments = chemical,
            preventiveMeasures = listOf(
                "Seed treatment with carbendazim or Pseudomonas before nursery sowing",
                "Maintain split application of potash (potassium) to strengthen cell walls",
                "Adopt blast-tolerant varieties (e.g. Swarna Sub1, IR64-Drt1)"
            ),
            sprayWindowRecommendation = "Spray in clear weather before 10 AM or after 4 PM with a fine mist nozzle.",
            languageCode = lang.code
        )
    }

    private fun getWheatYellowRust(lang: AppLanguage): DiseaseDiagnosis {
        val (cropName, diseaseName, summary) = when (lang) {
            AppLanguage.HINDI -> Triple(
                "गेहूं (Wheat)",
                "पीला रतुआ / हल्दी रोग (Stripe Rust)",
                "यह पक्सीनिया स्ट्राइफॉर्मिस (Puccinia striiformis) फफूंद से होता है। पत्तियों पर पीली हल्दी जैसे पाउडर की धारियां बन जाती हैं जो हाथ लगाने पर उंगलियों पर चिपकती हैं।"
            )
            AppLanguage.PUNJABI -> Triple(
                "ਕਣਕ (Wheat)",
                "ਪੀਲਾ ਰਤੂਆ / ਹਲਦੀ ਰੋਗ (Yellow Stripe Rust)",
                "ਇਹ ਕਣਕ ਦਾ ਬਹੁਤ ਤੇਜ਼ੀ ਨਾਲ ਫੈਲਣ ਵਾਲਾ ਰੋਗ ਹੈ। ਪੱਤਿਆਂ 'ਤੇ ਪੀਲੀਆਂ ਲਾਈਨਾਂ ਬਣ ਜਾਂਦੀਆਂ ਹਨ।"
            )
            AppLanguage.TELUGU -> Triple(
                "గోధుమ (Wheat)",
                "పసుపు కుంకుమ తెగులు (Stripe Rust)",
                "ఆకులపై పసుపు చారలు ఏర్పడతాయి, ఇవి పొడిలా రాలుతాయి."
            )
            AppLanguage.MARATHI -> Triple(
                "गहू (Wheat)",
                "पिवळा तांबेरा (Yellow Rust)",
                "पानांवर पिवळ्या रंगाच्या पट्ट्या तयार होतात, बुरशी हाताला पावडरसारखी लागते."
            )
            else -> Triple(
                "Wheat (Triticum aestivum)",
                "Yellow Stripe Rust (Puccinia striiformis)",
                "A devastating airborne fungal rust creating bright yellow-orange powdery pustules in parallel linear stripes along leaf veins."
            )
        }

        val chemical = listOf(
            TreatmentDetail(
                medicineName = if (lang == AppLanguage.HINDI) "प्रोपिकोनाज़ोल 25% EC (Propiconazole - Tilt)" else "Propiconazole 25% EC (Tilt)",
                dosage = "1.0 ml / Litre (200 ml in 200 Litres water / Acre)",
                instructions = "Direct systemic spray on wheat crop foliage as soon as yellow pustules appear.",
                safetyWarning = "Always wear face mask. Keep away from water bodies."
            ),
            TreatmentDetail(
                medicineName = if (lang == AppLanguage.HINDI) "टेबुकोनाज़ोल 25.9% EC" else "Tebuconazole 25.9% EC (Folicur)",
                dosage = "1.0 - 1.2 ml / Litre",
                instructions = "Excellent eradicative action if stripe rust has spread beyond 5% canopy.",
                safetyWarning = "Do not mix with alkaline substances."
            )
        )

        return DiseaseDiagnosis(
            cropName = cropName,
            cropScientificName = "Triticum aestivum",
            diseaseName = diseaseName,
            scientificName = "Puccinia striiformis",
            isHealthy = false,
            severityLevel = "Severe",
            confidenceScore = 97,
            summary = summary,
            symptoms = listOf(
                "Bright yellow powdery pustules arranged in long parallel stripes along leaf blades",
                "Yellow dust rubs off readily onto farmer fingers or white cloth",
                "Premature drying of flag leaves causing shrivelled grain filling"
            ),
            causes = listOf(
                "Cool temperatures (10-18°C) with persistent foggy mornings and high humidity",
                "Airborne urediniospores carried by winds from sub-mountainous tracks"
            ),
            organicTreatments = listOf(
                if (lang == AppLanguage.HINDI) "गोमूत्र और हींग का घोल (20 ग्राम हींग + 5 लीटर गोमूत्र / 100 लीटर पानी)" else "Fermented Asafoetida (Hing) & cow urine decoction (20g Hing in 100L water).",
                if (lang == AppLanguage.HINDI) "खट्टी छाछ (5-6 दिन पुरानी छाछ 5 लीटर प्रति एकड़) का छिड़काव" else "Sour buttermilk spray (5L per 100L water) to alter leaf surface pH."
            ),
            chemicalTreatments = chemical,
            preventiveMeasures = listOf(
                "Plant rust-resistant varieties like DBW 187, DBW 222, PBW 725, HD 3226",
                "Regular field scouting especially on northern borders and tree shade",
                "Avoid late sowing of wheat in rust-prone zones"
            ),
            sprayWindowRecommendation = "Spray immediately on a calm sunny day once fog lifts.",
            languageCode = lang.code
        )
    }

    private fun getPotatoLateBlight(lang: AppLanguage): DiseaseDiagnosis {
        val (cropName, diseaseName, summary) = when (lang) {
            AppLanguage.HINDI -> Triple(
                "आलू (Potato)",
                "पछेती झुलसा रोग (Late Blight)",
                "यह फाइटोफ्थोरा इन्फेस्टन्स (Phytophthora infestans) से होने वाला सबसे विनाशकारी रोग है। पत्तियों पर गीले काले धब्बे बनते हैं और निचली सतह पर सफेद फफूंद दिखती है।"
            )
            AppLanguage.PUNJABI -> Triple(
                "ਆਲੂ (Potato)",
                "ਪਛੇਤੀ ਝੁਲਸਾ ਰੋਗ (Late Blight)",
                "ਇਹ ਆਲੂ ਦਾ ਸਭ ਤੋਂ ਖਤਰਨਾਕ ਰੋਗ ਹੈ ਜੋ ਕੁਝ ਦਿਨਾਂ ਵਿੱਚ ਸਾਰੀ ਫਸਲ ਤਬਾਹ ਕਰ ਸਕਦਾ ਹੈ।"
            )
            AppLanguage.BENGALI -> Triple(
                "আলু (Potato)",
                "নাবী ধসা রোগ (Late Blight)",
                "পাতায় জলছাপযুক্ত বাদামী দাগ পড়ে ও পাতার নিচের দিকে সাদা ছত্রাক দেখা যায়।"
            )
            else -> Triple(
                "Potato (Solanum tuberosum)",
                "Late Blight (Phytophthora infestans)",
                "An aggressive oomycete disease causing rapid foliage necrosis, water-soaked lesions, white mildew and rotting tubers."
            )
        }

        val chemical = listOf(
            TreatmentDetail(
                medicineName = if (lang == AppLanguage.HINDI) "साइमोक्सानिल + मैंकोजेब (Curzate M-8)" else "Cymoxanil 8% + Mancozeb 64% WP",
                dosage = "2.5 - 3.0 g / Litre (600 g / Acre)",
                instructions = "Excellent translaminar curative spray within 48 hours of blight appearance.",
                safetyWarning = "Repeat after 7 days if weather remains cloudy and humid."
            ),
            TreatmentDetail(
                medicineName = if (lang == AppLanguage.HINDI) "डाइमेथोमॉर्फ 50% WP (Dimethomorph)" else "Dimethomorph 50% WP (Acrobat)",
                dosage = "1.0 - 1.2 g / Litre",
                instructions = "Systemic action protecting both leaves and developing underground tubers.",
                safetyWarning = "Wear protective goggles and respirator during preparation."
            )
        )

        return DiseaseDiagnosis(
            cropName = cropName,
            cropScientificName = "Solanum tuberosum",
            diseaseName = diseaseName,
            scientificName = "Phytophthora infestans",
            isHealthy = false,
            severityLevel = "Critical",
            confidenceScore = 98,
            summary = summary,
            symptoms = listOf(
                "Water-soaked dark purplish lesions on leaf margins and petioles",
                "Fine white downy mildew on underside of leaves in morning hours",
                "Foul rotting smell in field as foliage collapses rapidly",
                "Dry brown sunken rot inside harvested tubers"
            ),
            causes = listOf(
                "Cloudy overcast skies with temperatures between 12-20°C and > 90% humidity",
                "Infected seed tubers used without pre-treatment"
            ),
            organicTreatments = listOf(
                if (lang == AppLanguage.HINDI) "बोर्डो मिश्रण (Bordeaux Mixture 1%) का सुरक्षात्मक छिड़काव" else "Bordeaux mixture (1%) preventive foliar application.",
                if (lang == AppLanguage.HINDI) "संक्रमित पौधों को उखाड़कर गहरे गड्ढे में दबाएं" else "Rogue out infected haulms immediately to stop fieldwide dispersion."
            ),
            chemicalTreatments = chemical,
            preventiveMeasures = listOf(
                "Certified disease-free seed tuber sourcing",
                "High earthing up of ridges to prevent rain washing spores into tuber zone",
                "Dehaulming (cutting green tops) 12 days prior to tuber harvest"
            ),
            sprayWindowRecommendation = "Spray immediately before rain spells; spray again if washed away within 3 hours.",
            languageCode = lang.code
        )
    }

    private fun getCottonLeafCurl(lang: AppLanguage): DiseaseDiagnosis {
        val (cropName, diseaseName, summary) = when (lang) {
            AppLanguage.HINDI -> Triple(
                "कपास (Cotton)",
                "कपास पत्ता मरोड़िया विषाणु (Cotton Leaf Curl Virus)",
                "यह बेगोमोवायरस है जो सफेद मक्खी (Whitefly) द्वारा फैलता है। पत्तियों की नसें मोटी हो जाती हैं, पत्तियां ऊपर-नीचे मुड़ती हैं और प्याले जैसी हो जाती हैं।"
            )
            AppLanguage.PUNJABI -> Triple(
                "ਨਰਮਾ / ਕਪਾਹ (Cotton)",
                "ਚਿੱਟੀ ਮੱਖੀ ਤੇ ਪੱਤਾ ਮਰੋੜ ਰੋਗ (Cotton Leaf Curl)",
                "ਇਹ ਵਿਸ਼ਾਣੂ ਰੋਗ ਚਿੱਟੀ ਮੱਖੀ ਦੁਆਰਾ ਫੈਲਦਾ ਹੈ। ਪੱਤੇ ਮੁੜ ਜਾਂਦੇ ਹਨ ਅਤੇ ਬੂਟੇ ਦਾ ਵਾਧਾ ਰੁਕ ਜਾਂਦਾ ਹੈ।"
            )
            AppLanguage.GUJARATI -> Triple(
                "કપાસ (Cotton)",
                "કપાસનો પાન વળવાનો રોગ (Leaf Curl Virus)",
                "આ રોગ સફેદ માખી દ્વારા ફેલાય છે. પાંદડા વાંકાચૂંકા થઈ જાય છે."
            )
            else -> Triple(
                "Cotton (Gossypium hirsutum)",
                "Cotton Leaf Curl Virus (CLCuV)",
                "A geminivirus transmitted by whitefly (Bemisia tabaci) causing vein thickening, leaf enations and severe upward cup-shaped curling."
            )
        }

        val chemical = listOf(
            TreatmentDetail(
                medicineName = if (lang == AppLanguage.HINDI) "डायफेंथियूरॉन 50% WP (Diafenthiuron)" else "Diafenthiuron 50% WP (Pegasus)",
                dosage = "1.5 g / Litre (250 g / Acre)",
                instructions = "Potent insecticide to control vector whitefly nymphs and adults on lower leaf surfaces.",
                safetyWarning = "Toxic to bees; avoid spraying during active pollination hours."
            ),
            TreatmentDetail(
                medicineName = if (lang == AppLanguage.HINDI) "स्पाइरोमेसिफेन 22.9% SC (Oberon)" else "Spiromesifen 22.9% SC (Oberon)",
                dosage = "1.0 ml / Litre (200 ml / Acre)",
                instructions = "Inhibits lipid biosynthesis in whitefly nymphs preventing population explosion.",
                safetyWarning = "Maintain safe waiting period before boll opening."
            )
        )

        return DiseaseDiagnosis(
            cropName = cropName,
            cropScientificName = "Gossypium hirsutum",
            diseaseName = diseaseName,
            scientificName = "Cotton leaf curl virus (CLCuV)",
            isHealthy = false,
            severityLevel = "High",
            confidenceScore = 93,
            summary = summary,
            symptoms = listOf(
                "Swollen dark green thickened veins on ventral leaf surface",
                "Upward or downward cupping and crinkling of leaves",
                "Cup-shaped leaf outgrowths (enations) on leaf undersides",
                "Severe stunting and reduction in boll formation"
            ),
            causes = listOf(
                "High whitefly (Bemisia tabaci) population acting as viral transmission vector",
                "Alternate weed hosts (such as Parthenium, Abutilon) growing on field borders"
            ),
            organicTreatments = listOf(
                if (lang == AppLanguage.HINDI) "पीले चिपचिपे कार्ड (Yellow Sticky Traps) 15-20 प्रति एकड़ लगाएं।" else "Install yellow sticky traps (15-20 per acre) at canopy height to trap whiteflies.",
                if (lang == AppLanguage.HINDI) "नीम आधारित कीटनाशक (1500 ppm) 5 मिली प्रति लीटर का स्प्रे।" else "Spray 1500 ppm neem formulation at 5 ml/L to deter egg laying.",
                if (lang == AppLanguage.HINDI) "मेढ़ों पर से गाजरघास और खरपतवार नष्ट करें।" else "Clear broadleaf weeds around borders."
            ),
            chemicalTreatments = chemical,
            preventiveMeasures = listOf(
                "Sow tolerant Bt hybrids recommended for your agro-climatic zone",
                "Avoid growing American cotton varieties near orchards or perennial host trees",
                "Synchronized timely sowing in early May"
            ),
            sprayWindowRecommendation = "Spray in calm morning weather ensuring thorough wetting under leaf surfaces.",
            languageCode = lang.code
        )
    }

    private fun getMaizeArmyworm(lang: AppLanguage): DiseaseDiagnosis {
        val (cropName, diseaseName, summary) = when (lang) {
            AppLanguage.HINDI -> Triple(
                "मक्का / भुट्टा (Maize)",
                "फॉल आर्मीवर्म एवं कण्डवा (Fall Armyworm)",
                "यह विनाशकारी कीट (Spodoptera frugiperda) मक्के के पोंगली (गोभ) में छिपकर पत्तियों को जालीदार बना देता है और बुरादे जैसा मल छोड़ता है।"
            )
            AppLanguage.PUNJABI -> Triple(
                "ਮੱਕੀ (Maize)",
                "ਫਾਲ ਆਰਮੀਵਰਮ ਸੁੰਡੀ (Fall Armyworm)",
                "ਸੁੰਡੀ ਪੌਦੇ ਦੀ ਗੋਭ ਵਿੱਚ ਵੜ ਕੇ ਪੱਤਿਆਂ ਨੂੰ ਖਾਂਦੀ ਹੈ।"
            )
            else -> Triple(
                "Maize / Corn (Zea mays)",
                "Fall Armyworm (Spodoptera frugiperda)",
                "A voracious invasive pest whose larvae feed deep inside the whorl, causing windowpaning, shot-holes and sawdust-like frass."
            )
        }

        val chemical = listOf(
            TreatmentDetail(
                medicineName = if (lang == AppLanguage.HINDI) "क्लोरांट्रानिलीप्रोल 18.5% SC (Coragen)" else "Chlorantraniliprole 18.5% SC (Coragen)",
                dosage = "0.4 ml / Litre (60 ml / Acre)",
                instructions = "Direct the spray nozzle straight down into the plant central whorl (gobb).",
                safetyWarning = "Extremely effective; do not exceed prescribed dosage."
            ),
            TreatmentDetail(
                medicineName = if (lang == AppLanguage.HINDI) "इमामेक्टिन बेंजोएट 5% SG (Proclaim)" else "Emamectin Benzoate 5% SG",
                dosage = "0.5 g / Litre (100 g / Acre)",
                instructions = "Target early instar larvae inside whorl at 20-25 days after germination.",
                safetyWarning = "Wear protective overalls and mask."
            )
        )

        return DiseaseDiagnosis(
            cropName = cropName,
            cropScientificName = "Zea mays",
            diseaseName = diseaseName,
            scientificName = "Spodoptera frugiperda",
            isHealthy = false,
            severityLevel = "Severe",
            confidenceScore = 95,
            summary = summary,
            symptoms = listOf(
                "Large irregular feeding holes in emerging leaves",
                "Copious coarse sawdust-like frass accumulated in leaf whorls",
                "Severely shredded ragged leaf margins",
                "Damaged tasselling and ear borers"
            ),
            causes = listOf(
                "Nocturnal female moths flying long distances and laying egg masses covered with felt scales",
                "Monoculture planting without intercropping barriers"
            ),
            organicTreatments = listOf(
                if (lang == AppLanguage.HINDI) "गोभ में सूखी रेत व राख (9:1 अनुपात) या चूना डालें।" else "Pour dry sand/ash mixture (9:1) directly into central leaf whorls.",
                if (lang == AppLanguage.HINDI) "बैसिलस थुरिंजिएंसिस (Bt) 2 ग्राम प्रति लीटर पानी में मिलाकर स्प्रे करें।" else "Apply Bacillus thuringiensis (Bt) kurstaki bio-insecticide @ 2g/L.",
                if (lang == AppLanguage.HINDI) "फेरोमोन ट्रैप (Pheromone Traps) 5 प्रति एकड़ लगाएं।" else "Set up 5 pheromone traps per acre for adult moth monitoring."
            ),
            chemicalTreatments = chemical,
            preventiveMeasures = listOf(
                "Intercrop with pulses (cowpea or pigeonpea) to enhance natural predator wasps",
                "Deep summer ploughing to expose pupae to predatory birds",
                "Early scouting at knee-high crop stage"
            ),
            sprayWindowRecommendation = "Direct application into plant central whorl late afternoon when larvae are active.",
            languageCode = lang.code
        )
    }

    private fun getChilliLeafCurl(lang: AppLanguage): DiseaseDiagnosis {
        val (cropName, diseaseName, summary) = when (lang) {
            AppLanguage.HINDI -> Triple(
                "मिर्च (Chilli / Pepper)",
                "चुर्रा-मुर्रा रोग (Chilli Thrips & Mites)",
                "रस चूसक कीटों (थ्रिप्स व माइट्स) के प्रकोप से पत्तियां नाव की तरह ऊपर या नीचे मुड़ जाती हैं, पौधे बौने रह जाते हैं और फूल झड़ने लगते हैं।"
            )
            AppLanguage.PUNJABI -> Triple(
                "ਮਿਰਚ (Chilli)",
                "ਮਿਰਚਾਂ ਦਾ ਚੁਰੜ-ਮੁਰੜ ਰੋਗ (Thrips & Mites)",
                "ਕੀੜਿਆਂ ਦੇ ਹਮਲੇ ਕਾਰਨ ਪੱਤੇ ਉੱਪਰ ਨੂੰ ਮੁੜ ਜਾਂਦੇ ਹਨ।"
            )
            AppLanguage.TELUGU -> Triple(
                "మిరప (Chilli)",
                "మిరప బొబ్బర తెగులు (Chilli Murda)",
                "తామర పురుగులు, నల్లి పురుగుల వల్ల ఆకులు పైకి లేదా కిందికి ముడుచుకుంటాయి."
            )
            else -> Triple(
                "Chilli Pepper (Capsicum annuum)",
                "Chilli Leaf Curl & Murda Complex",
                "Sucking pests (Scirtothrips dorsalis & Polyphagotarsonemus latus) causing boat-shaped upward and downward cupping, bronze discoloration and rosette appearance."
            )
        }

        val chemical = listOf(
            TreatmentDetail(
                medicineName = if (lang == AppLanguage.HINDI) "फिप्रोनिल 5% SC (Regent)" else "Fipronil 5% SC",
                dosage = "2.0 ml / Litre (400 ml / Acre)",
                instructions = "Targets thrips on upper tender shoots and flowers.",
                safetyWarning = "Apply before flower bloom opens."
            ),
            TreatmentDetail(
                medicineName = if (lang == AppLanguage.HINDI) "प्रोपरगाइट 57% EC (Omite)" else "Propargite 57% EC (Omite)",
                dosage = "2.5 ml / Litre",
                instructions = "Effective acaricide specifically against downward curling caused by yellow mites.",
                safetyWarning = "Do not spray during extreme midday heat."
            )
        )

        return DiseaseDiagnosis(
            cropName = cropName,
            cropScientificName = "Capsicum annuum",
            diseaseName = diseaseName,
            scientificName = "Scirtothrips dorsalis / Polyphagotarsonemus latus",
            isHealthy = false,
            severityLevel = "Moderate",
            confidenceScore = 94,
            summary = summary,
            symptoms = listOf(
                "Boat-shaped upward curling of leaves due to thrips feeding",
                "Downward inverted spoon curling with bronzing caused by mites",
                "Brittle leaves with shortened internodes giving a bushy appearance",
                "Heavy flower and young fruit drop"
            ),
            causes = listOf(
                "Dry hot weather spells favoring explosive thrips multiplication",
                "High nitrogen fertilizer use producing tender succulent shoots"
            ),
            organicTreatments = listOf(
                if (lang == AppLanguage.HINDI) "दशपर्णी अर्क या 5% नीम अर्क का 7 दिन पर छिड़काव।" else "Spray 5% Neem seed kernel extract (NSKE) or Dashparni ark every 7 days.",
                if (lang == AppLanguage.HINDI) "नीले व पीले चिपचिपे ट्रैप 10-10 प्रति एकड़ लगाएं।" else "Deploy blue sticky traps for thrips and yellow traps for whiteflies (10 each/acre)."
            ),
            chemicalTreatments = chemical,
            preventiveMeasures = listOf(
                "Barrier cropping with 2-3 rows of maize or sorghum around chilli plots",
                "Avoid planting chilli next to onion or garlic fields",
                "Maintain optimal field moisture"
            ),
            sprayWindowRecommendation = "Spray early morning with underleaf spray coverage.",
            languageCode = lang.code
        )
    }

    private fun getHealthyLeaf(lang: AppLanguage): DiseaseDiagnosis {
        val (cropName, diseaseName, summary) = when (lang) {
            AppLanguage.HINDI -> Triple(
                "स्वस्थ फसल (Healthy Crop Leaf)",
                "कोई रोग नहीं मिला (Healthy Crop)",
                "आपकी फसल की पत्तियां बिल्कुल स्वस्थ हैं! पत्तियों का रंग गहरा हरा, चमकदार और बनावट सामान्य है। कोई कीट या फफूंद के लक्षण नहीं मिले हैं।"
            )
            AppLanguage.PUNJABI -> Triple(
                "ਸਿਹਤਮੰਦ ਫਸਲ (Healthy Crop Leaf)",
                "ਕੋਈ ਬਿਮਾਰੀ ਨਹੀਂ ਹੈ (Healthy Crop)",
                "ਤੁਹਾਡੀ ਫਸਲ ਬਿਲਕੁਲ ਸਿਹਤਮੰਦ ਅਤੇ ਹਰੀ-ਭਰੀ ਹੈ।"
            )
            AppLanguage.TELUGU -> Triple(
                "ఆరోగ్యకరమైన పంట (Healthy Crop Leaf)",
                "ఏ వ్యాధి లేదు (Healthy Crop)",
                "మీ పంట ఆకులు చాలా ఆరోగ్యంగా ఉన్నాయి. ఎలాంటి తెగుళ్లు లేవు."
            )
            AppLanguage.TAMIL -> Triple(
                "ஆரோக்கியமான பயிர் (Healthy Crop)",
                "நோய் எதுவும் இல்லை (Healthy Crop)",
                "உங்கள் பயிர் ஆரோக்கியமாக உள்ளது. எந்த பூச்சியோ நோயோ இல்லை."
            )
            AppLanguage.BENGALI -> Triple(
                "সুস্থ ফসল (Healthy Crop)",
                "কোন রোগ নেই (Healthy Crop)",
                "আপনার ফসলের পাতা সম্পূর্ণ সুস্থ ও রোগমুক্ত।"
            )
            AppLanguage.MARATHI -> Triple(
                "निरोगी पीक (Healthy Crop)",
                "कोणताही रोग नाही (Healthy Crop)",
                "तुमचे पीक पूर्णपणे निरोगी आणि टवटवीत आहे."
            )
            AppLanguage.GUJARATI -> Triple(
                "તંદુરસ્ત પાક (Healthy Crop)",
                "કોઈ રોગ નથી (Healthy Crop)",
                "તમારો પાક સંપૂર્ણપણે તંદુરસ્ત છે."
            )
            AppLanguage.SPANISH -> Triple(
                "Cultivo Saludable (Healthy Crop)",
                "Sin Enfermedades Detectadas",
                "¡El follaje está sano y vigoroso! No se aprecian signos de hongos, plagas ni deficiencias nutricionales."
            )
            AppLanguage.FRENCH -> Triple(
                "Culture Saine (Healthy Crop)",
                "Aucune Maladie Détectée",
                "Le feuillage est vigoureux et en parfaite santé. Aucun symptôme de ravageur ou maladie fongique."
            )
            AppLanguage.SWAHILI -> Triple(
                "Zao lenye Afya (Healthy Crop)",
                "Hakuna Ugonjwa Uliogunduliwa",
                "Majani ya mmea wako yana afya nzuri na hayana dalili za ugonjwa."
            )
            else -> Triple(
                "Healthy Plant Leaf",
                "Healthy - No Pathogens Detected",
                "Great news! The plant foliage appears vigorous, green, and completely free from detectable lesions, spots, rusts or pest infestations."
            )
        }

        return DiseaseDiagnosis(
            cropName = cropName,
            cropScientificName = "Plantae",
            diseaseName = diseaseName,
            scientificName = "Healthy",
            isHealthy = true,
            severityLevel = "Healthy",
            confidenceScore = 99,
            summary = summary,
            symptoms = listOf(
                "Uniform green chlorophyll pigmentation",
                "Normal vein architecture without chlorosis or necrosis",
                "Smooth lamina with no puckering or spots"
            ),
            causes = emptyList(),
            organicTreatments = listOf(
                if (lang == AppLanguage.HINDI) "पौधे की रोग प्रतिरोधक क्षमता बनाए रखने के लिए जीवामृत का प्रयोग करें।" else "Apply Jeevamrutha or seaweed extract every 15 days to maintain plant vitality.",
                if (lang == AppLanguage.HINDI) "संतुलित सिंचाई और समय पर निराई-गुड़ाई करते रहें।" else "Continue balanced drip irrigation and regular weed control."
            ),
            chemicalTreatments = emptyList(),
            preventiveMeasures = listOf(
                "Scout your crop weekly to catch any early outbreaks early",
                "Maintain balanced N-P-K fertilizer application with micronutrients",
                "Keep bunds and field irrigation channels clean"
            ),
            sprayWindowRecommendation = "No chemical sprays needed. Continue regular care.",
            languageCode = lang.code
        )
    }
}
