package com.example.data.repository

import com.example.data.model.*

object OdysafeRepository {

    val sampleDestinations = listOf(
        Destination(
            id = "madurai",
            name = "Madurai",
            state = "Tamil Nadu",
            category = DestinationCategory.SPIRITUAL,
            tagline = "The ancient temple city of cultural heritage and vibrant bazaars",
            description = "Madurai is one of India's oldest continuously inhabited cities, centered around the historic Meenakshi Amman Temple complex. Rich in Dravidian architecture, bustling morning markets, and cultural pilgrimages.",
            bestTimeToVisit = "October to March (Pleasant 22°C - 30°C)",
            accessibilityRating = "Paved perimeter with ramps around Meenakshi East & West Gates; central sanctum requires walking.",
            entryFee = "Free entry to temple; camera token ₹50",
            openingHours = "05:00 AM – 12:30 PM, 04:00 PM – 09:30 PM",
            safetyStatus = "Normal safety conditions. Standard crowd advisory.",
            safetySeverity = AlertSeverity.INFO,
            safetyNotice = "Footwear must be deposited at designated stalls. Beware of non-registered guides.",
            safetySource = "Tamil Nadu Tourist Police & District Administration",
            safetyTimestamp = "Verified today at 08:30 AM IST",
            transportHighlights = listOf("Direct trains from Chennai & Bangalore", "Madurai Int'l Airport (12 km from center)", "Prepaid auto-rickshaws at junction"),
            highlights = listOf("Meenakshi Sundareswarar Temple", "Thirumalai Nayakkar Mahal", "Gandhi Memorial Museum", "Vandiyur Mariamman Teppakulam"),
            suggestedItinerary = listOf(
                "Day 1 Morning: Temple inner shrines & Hall of Thousand Pillars",
                "Day 1 Afternoon: Palace architecture & light show",
                "Day 2: Gandhi Museum & evening local street culinary tour"
            ),
            latitude = 9.9195,
            longitude = 78.1193,
            isSaved = true
        ),
        Destination(
            id = "chennai",
            name = "Chennai",
            state = "Tamil Nadu",
            category = DestinationCategory.BEACHES,
            tagline = "Gateway to South India blending seaside heritage with modern arts",
            description = "Chennai offers vibrant coastal culture, the world's second-longest urban beach (Marina), historic Fort St. George, and traditional music academies.",
            bestTimeToVisit = "November to February",
            accessibilityRating = "Marina Beach has a dedicated permanent wooden ramp walkway for wheelchair users near Gandhi Statue.",
            entryFee = "Free for beaches; Fort Museum ₹25 (Indian citizens)",
            openingHours = "Beaches open 24/7; Marina promenade safest between 05:00 AM and 09:00 PM",
            safetyStatus = "High Tide Advisory for Marina & Besant Nagar coastline.",
            safetySeverity = AlertSeverity.CAUTION,
            safetyNotice = "Strong rip currents reported. Swimming strictly prohibited by Coastal Security Group.",
            safetySource = "Coastal Security Group & IMD Regional Met Center",
            safetyTimestamp = "Updated 2 hours ago",
            transportHighlights = listOf("Chennai Central & Egmore Railway Stations", "Metro Line connecting Airport to Central", "Bus & suburban rail network"),
            highlights = listOf("Marina Beach Promenade", "Kapaleeshwarar Temple, Mylapore", "San Thome Cathedral Basilica", "DakshinaChitra Heritage Village"),
            suggestedItinerary = listOf(
                "Day 1: Mylapore cultural walk, Kapaleeshwarar Temple & San Thome",
                "Day 2: Morning walk at Besant Nagar, DakshinaChitra cultural museum"
            ),
            latitude = 13.0827,
            longitude = 80.2707,
            isSaved = false
        ),
        Destination(
            id = "ooty",
            name = "Ooty (Udhagamandalam)",
            state = "Tamil Nadu",
            category = DestinationCategory.HILLS,
            tagline = "Queen of Nilgiri Hill Stations with misty tea plantations and lakes",
            description = "Perched at 2,240m in the Nilgiris, Ooty features eucalyptus groves, terraced tea gardens, botanical collections, and the UNESCO heritage Nilgiri Mountain Railway.",
            bestTimeToVisit = "March to June and September to November",
            accessibilityRating = "Botanical Gardens have paved main pathways; hill viewpoints have uneven terrain.",
            entryFee = "Botanical Gardens ₹40; Rose Garden ₹40",
            openingHours = "07:00 AM – 06:30 PM",
            safetyStatus = "Dense Morning Fog on Ghat Roads (Kotagiri & Coonoor passes).",
            safetySeverity = AlertSeverity.CAUTION,
            safetyNotice = "Use low-beam headlights. Hill hairpin bends require cautious speed limits under 30 km/h.",
            safetySource = "Nilgiris District Traffic Police & Disaster Cell",
            safetyTimestamp = "Verified today at 06:00 AM IST",
            transportHighlights = listOf("Nilgiri Mountain Railway Toy Train from Mettupalayam", "Direct buses from Coimbatore (88 km)", "Ghat road checkpoints enforce e-pass when applicable"),
            highlights = listOf("Government Botanical Garden", "Ooty Lake & Boating", "Doddabetta Peak Viewpoint", "Tea Factory and Museum"),
            suggestedItinerary = listOf(
                "Day 1: Toy train arrival, Botanical Garden, Lake boating at sunset",
                "Day 2: Doddabetta peak viewpoint & Tea Factory tour"
            ),
            latitude = 11.4102,
            longitude = 76.6950,
            isSaved = true
        ),
        Destination(
            id = "rameswaram",
            name = "Rameswaram",
            state = "Tamil Nadu",
            category = DestinationCategory.SPIRITUAL,
            tagline = "Sacred island temple sanctuary and the edge of India at Dhanushkodi",
            description = "Connected to mainland India by the magnificent Pamban bridge, Rameswaram is home to Ramanathaswamy Temple's 22 sacred teerthams and the ghost town of Dhanushkodi.",
            bestTimeToVisit = "October to April",
            accessibilityRating = "Ramanathaswamy temple complex has level stone corridors with shaded wheelchair passages.",
            entryFee = "Temple entry free; Agni Teertham open public access",
            openingHours = "05:00 AM – 01:00 PM, 03:00 PM – 09:00 PM",
            safetyStatus = "Strong coastal crosswinds near Dhanushkodi sand spit.",
            safetySeverity = AlertSeverity.CAUTION,
            safetyNotice = "Entry to Dhanushkodi beach restricted after 05:30 PM. Follow maritime coast guard guidance.",
            safetySource = "Ramanathapuram Police & Indian Coast Guard",
            safetyTimestamp = "Verified today at 09:15 AM IST",
            transportHighlights = listOf("Pamban railway & roadway bridges", "Direct state transport express buses", "Local authorized 4WD shuttles to Dhanushkodi"),
            highlights = listOf("Ramanathaswamy Temple 1000-pillar corridor", "Agni Teertham seashore", "Dhanushkodi Ghost Town & Ram Setu viewpoint", "APJ Abdul Kalam National Memorial"),
            suggestedItinerary = listOf(
                "Day 1: Ramanathaswamy sacred wells bath & temple visit",
                "Day 2: Early morning sunrise trip to Dhanushkodi & Kalam Memorial"
            ),
            latitude = 9.2876,
            longitude = 79.3129,
            isSaved = false
        ),
        Destination(
            id = "jaipur",
            name = "Jaipur",
            state = "Rajasthan",
            category = DestinationCategory.HERITAGE,
            tagline = "The Pink City of majestic hilltop forts and royal Rajput palaces",
            description = "Rajasthan's capital is a UNESCO World Heritage city planned according to Vastu Shastra, renowned for Amer Fort, Hawa Mahal, and colourful Johari and Bapu bazaars.",
            bestTimeToVisit = "November to February",
            accessibilityRating = "City Palace offers golf cart assistance and ramps; Amer Fort steep cobblestones require vehicle drop-off at upper courtyard.",
            entryFee = "Composite monument ticket ₹100 for Indian nationals, ₹500 for foreign tourists",
            openingHours = "09:00 AM – 05:00 PM (Amer Fort light show 07:00 PM)",
            safetyStatus = "Normal conditions. Keep hydrated during midday sun.",
            safetySeverity = AlertSeverity.INFO,
            safetyNotice = "Use authorized Rajasthan Tourism development guides with official photo ID badges.",
            safetySource = "Rajasthan Tourism Police & Jaipur Commissionerate",
            safetyTimestamp = "Updated 4 hours ago",
            transportHighlights = listOf("Jaipur International Airport (JAI)", "Extensive train links across North India", "Jaipur Metro line 1 linking old city"),
            highlights = listOf("Amer Fort & Palace", "Hawa Mahal (Palace of Winds)", "Jantar Mantar Astronomical Observatory", "City Palace Museum"),
            suggestedItinerary = listOf(
                "Day 1: Amer Fort, Jaigarh Fort & Jal Mahal view",
                "Day 2: Hawa Mahal, Jantar Mantar, City Palace & Johari Bazaar"
            ),
            latitude = 26.9124,
            longitude = 75.7873,
            isSaved = false
        ),
        Destination(
            id = "kochi",
            name = "Kochi (Cochin)",
            state = "Kerala",
            category = DestinationCategory.HERITAGE,
            tagline = "Historic spice port with Chinese fishing nets and art-filled colonial lanes",
            description = "A melting pot of Portuguese, Dutch, British, and Arab influences across the Arabian Sea coast, known for Fort Kochi, Mattancherry synagogue, and water metro ferries.",
            bestTimeToVisit = "September to March",
            accessibilityRating = "Water Metro boats and terminals are fully barrier-free with level gangways.",
            entryFee = "Mattancherry Palace ₹5; Jewish Synagogue ₹10",
            openingHours = "10:00 AM – 05:00 PM (Synagogue closed Fridays & Saturdays)",
            safetyStatus = "Calm coastal conditions. Normal precautions.",
            safetySeverity = AlertSeverity.INFO,
            safetyNotice = "Do not board unregistered private boat operators on Vembanad Lake.",
            safetySource = "Kerala Tourism Police & Cochin Port Authority",
            safetyTimestamp = "Verified today at 07:45 AM IST",
            transportHighlights = listOf("Cochin International Airport (COK) - world's 1st solar airport", "Kochi Water Metro integrated transit", "Ernakulam Junction railway station"),
            highlights = listOf("Fort Kochi Beach & Chinese Fishing Nets", "Jew Town & Paradesi Synagogue", "Mattancherry Dutch Palace", "Kathakali Cultural Dance Centers"),
            suggestedItinerary = listOf(
                "Day 1: Fort Kochi walking tour, St. Francis Church, Chinese Nets sunset",
                "Day 2: Water Metro ride, Jew Town antique shops & Mattancherry Palace"
            ),
            latitude = 9.9312,
            longitude = 76.2673,
            isSaved = true
        ),
        Destination(
            id = "varanasi",
            name = "Varanasi",
            state = "Uttar Pradesh",
            category = DestinationCategory.SPIRITUAL,
            tagline = "The world's oldest living spiritual capital along the sacred Ganga",
            description = "Varanasi (Kashi) is celebrated for its spiritual river ghats, labyrinthine ancient alleys, silk weaving, and the nightly Ganga Aarti ceremonial prayers.",
            bestTimeToVisit = "October to March",
            accessibilityRating = "Steep stone steps along ghats; electric golf carts operate between Godowlia and Dashashwamedh for elderly & disabled visitors.",
            entryFee = "Ghats free; Kashi Vishwanath temple corridor entry free (special pooja tickets separate)",
            openingHours = "Ghats accessible 24/7; Evening Ganga Aarti starts around 06:45 PM",
            safetyStatus = "High evening pedestrian density at Dashashwamedh Ghat.",
            safetySeverity = AlertSeverity.CAUTION,
            safetyNotice = "Keep children and belongings close. Only use licensed boatmen with standard life jackets.",
            safetySource = "Varanasi Tourist Police & Ganga Task Force",
            safetyTimestamp = "Updated 1 hour ago",
            transportHighlights = listOf("Lal Bahadur Shastri Airport (25 km)", "Varanasi Cantt & Banaras Railway Stations", "Kashi Vishwanath Ropeway (under development)"),
            highlights = listOf("Dashashwamedh Ghat Evening Aarti", "Kashi Vishwanath Temple Corridor", "Assi Ghat morning yoga & subah-e-banaras", "Sarnath Buddhist Archaeological Site (10 km)"),
            suggestedItinerary = listOf(
                "Day 1: Sunrise boat cruise, Vishwanath temple corridor, Evening Aarti",
                "Day 2: Excursion to Sarnath stupas and Varanasi weaver colonies"
            ),
            latitude = 25.3176,
            longitude = 82.9739,
            isSaved = false
        ),
        Destination(
            id = "munnar",
            name = "Munnar",
            state = "Kerala",
            category = DestinationCategory.NATURE,
            tagline = "Rolling emerald tea slopes, misty gorges, and home of the Nilgiri Tahr",
            description = "Nestled at the confluence of three mountain streams in the Western Ghats, Munnar is famous for sprawling tea estates, cool mountain air, waterfalls, and Eravikulam National Park.",
            bestTimeToVisit = "September to May",
            accessibilityRating = "Tea Museum ground floor accessible; National park safari bus has step entry.",
            entryFee = "Eravikulam National Park ₹200 (Indians); Tea Museum ₹125",
            openingHours = "07:30 AM – 04:00 PM for national park",
            safetyStatus = "Landslide caution advisory along Gap Road stretch during heavy showers.",
            safetySeverity = AlertSeverity.CAUTION,
            safetyNotice = "Avoid night travel after 08:00 PM on Munnar-Bodimettu highway due to sudden thick fog.",
            safetySource = "Idukki District Disaster Management Authority (DDMA)",
            safetyTimestamp = "Verified today at 10:00 AM IST",
            transportHighlights = listOf("Kochi Airport to Munnar (110 km scenic hill drive)", "KSRTC hill buses from Ernakulam & Kottayam", "Private taxis with trained hill drivers"),
            highlights = listOf("Eravikulam National Park (Rajamalai)", "Tata Tea Museum & Tea Processing Tour", "Mattupetty Dam & Eco Point", "Top Station valley viewpoints"),
            suggestedItinerary = listOf(
                "Day 1: Tea museum, Tea plantation nature walk & Mattupetty Lake",
                "Day 2: Eravikulam Nilgiri Tahr safari & Top Station sunset"
            ),
            latitude = 10.0889,
            longitude = 77.0595,
            isSaved = true
        )
    )

    val sampleNearbyServices = listOf(
        // Hospitals
        NearbyService(
            id = "hosp_1",
            name = "Apollo Speciality Hospital",
            type = ServiceType.HOSPITAL,
            address = "Lake View Road, K.K. Nagar, Madurai, TN",
            distanceKm = 1.8,
            phone = "+91 452 258 0888",
            isEmergencyFacility = true,
            openHours = "24/7 Emergency & Trauma",
            latitude = 9.9240,
            longitude = 78.1380
        ),
        NearbyService(
            id = "hosp_2",
            name = "Government Rajaji Hospital (GRH)",
            type = ServiceType.HOSPITAL,
            address = "Panagal Road, Shenoy Nagar, Madurai, TN",
            distanceKm = 2.4,
            phone = "+91 452 253 2535",
            isEmergencyFacility = true,
            openHours = "24/7 Government Trauma Center",
            latitude = 9.9320,
            longitude = 78.1290
        ),
        NearbyService(
            id = "hosp_3",
            name = "Meenakshi Mission Hospital & Research Centre",
            type = ServiceType.HOSPITAL,
            address = "Melur Main Road, Madurai, TN",
            distanceKm = 4.6,
            phone = "+91 452 426 3000",
            isEmergencyFacility = true,
            openHours = "24/7 Multi-speciality Emergency",
            latitude = 9.9500,
            longitude = 78.1600
        ),
        // Pharmacies
        NearbyService(
            id = "pharm_1",
            name = "Apollo Pharmacy (24 Hours)",
            type = ServiceType.PHARMACY,
            address = "Near Periyar Bus Stand, West Tower Street, Madurai",
            distanceKm = 0.6,
            phone = "+91 452 234 1122",
            isEmergencyFacility = false,
            openHours = "Open 24 Hours",
            latitude = 9.9180,
            longitude = 78.1150
        ),
        NearbyService(
            id = "pharm_2",
            name = "MedPlus Town Hall Pharmacy",
            type = ServiceType.PHARMACY,
            address = "Town Hall Road, Near Meenakshi Temple South Gate",
            distanceKm = 0.4,
            phone = "+91 452 233 4455",
            isEmergencyFacility = false,
            openHours = "07:00 AM – 11:00 PM",
            latitude = 9.9170,
            longitude = 78.1180
        ),
        // Police
        NearbyService(
            id = "pol_1",
            name = "Tourist Police Assistance Booth (Meenakshi Temple)",
            type = ServiceType.POLICE,
            address = "East Chithirai Street, Opposite Rayar Gopuram, Madurai",
            distanceKm = 0.3,
            phone = "+91 452 234 4300",
            isEmergencyFacility = true,
            openHours = "24/7 Dedicated Tourist Assistance Desk",
            latitude = 9.9200,
            longitude = 78.1210
        ),
        NearbyService(
            id = "pol_2",
            name = "Madurai Central Police Station (B1)",
            type = ServiceType.POLICE,
            address = "Vilakkuthoon, South Masi Street, Madurai",
            distanceKm = 1.1,
            phone = "+91 452 233 8000",
            isEmergencyFacility = true,
            openHours = "24/7 Emergency Station (Dial 112 / 100)",
            latitude = 9.9140,
            longitude = 78.1230
        ),
        // Stays
        NearbyService(
            id = "stay_1",
            name = "Heritage Madurai Hotel",
            type = ServiceType.HOTEL,
            address = "11 Park Road, Pasumalai, Madurai",
            distanceKm = 3.5,
            phone = "+91 452 238 5455",
            isEmergencyFacility = false,
            openHours = "Front Desk 24/7",
            latitude = 9.9050,
            longitude = 78.0850
        ),
        NearbyService(
            id = "stay_2",
            name = "The Gateway Hotel Pasumalai",
            type = ServiceType.HOTEL,
            address = "Pasumalai Hill, Madurai",
            distanceKm = 4.1,
            phone = "+91 452 663 3000",
            isEmergencyFacility = false,
            openHours = "Front Desk 24/7",
            latitude = 9.8980,
            longitude = 78.0820
        ),
        // Food
        NearbyService(
            id = "food_1",
            name = "Murugan Idli Shop",
            type = ServiceType.RESTAURANT,
            address = "West Masi Street, Madurai",
            distanceKm = 0.5,
            phone = "+91 452 234 1379",
            isEmergencyFacility = false,
            openHours = "07:00 AM – 11:00 PM",
            latitude = 9.9190,
            longitude = 78.1160
        ),
        NearbyService(
            id = "food_2",
            name = "Sree Sabarees Pure Veg",
            type = ServiceType.RESTAURANT,
            address = "Opposite Central Railway Station, Madurai",
            distanceKm = 0.9,
            phone = "+91 452 234 8888",
            isEmergencyFacility = false,
            openHours = "06:30 AM – 10:30 PM",
            latitude = 9.9210,
            longitude = 78.1120
        ),
        // Attractions
        NearbyService(
            id = "att_1",
            name = "Meenakshi Amman Temple Complex",
            type = ServiceType.ATTRACTION,
            address = "Madurai Main Center",
            distanceKm = 0.2,
            phone = "+91 452 234 4360",
            isEmergencyFacility = false,
            openHours = "05:00 AM – 12:30 PM, 04:00 PM – 09:30 PM",
            latitude = 9.9195,
            longitude = 78.1193
        ),
        NearbyService(
            id = "att_2",
            name = "Thirumalai Nayakkar Mahal",
            type = ServiceType.ATTRACTION,
            address = "Panthadi, Madurai",
            distanceKm = 1.3,
            phone = "+91 452 233 2943",
            isEmergencyFacility = false,
            openHours = "09:00 AM – 05:00 PM (Light show 06:45 PM)",
            latitude = 9.9150,
            longitude = 78.1250
        )
    )

    val sampleSafetyAlerts = listOf(
        SafetyAlert(
            id = "alert_1",
            title = "Dense Morning Fog Advisory on Nilgiri Ghats",
            description = "Visibility drop below 50m recorded on Ooty-Mettupalayam ghat curves 12 through 28 between 05:00 AM and 09:00 AM.",
            affectedArea = "Ooty, Coonoor & Kotagiri Ghat roads",
            severity = AlertSeverity.CAUTION,
            category = AlertCategory.WEATHER,
            source = "India Meteorological Dept (IMD) & Nilgiris Police",
            timestamp = "Today at 06:15 AM IST",
            recommendedActions = listOf(
                "Keep headlights on low beam and fog lamps engaged",
                "Maintain minimum 20m following distance between hill vehicles",
                "Do not overtake on designated blind hairpin bends",
                "Delay departure until after 08:30 AM if traveling by two-wheeler"
            ),
            safeAlternative = "Travel via Kotagiri route which has lower incline and wider shoulder stopping bays."
        ),
        SafetyAlert(
            id = "alert_2",
            title = "High Tide & Rip Current Warning — Marina Beach",
            description = "Astronomical high tide accompanied by strong undertows observed. Maritime security has deployed red flags along the shoreline.",
            affectedArea = "Chennai Marina & Besant Nagar Beachfront",
            severity = AlertSeverity.DANGER,
            category = AlertCategory.OFFICIAL,
            source = "Coastal Security Group (CSG) & INCOIS Hyderabad",
            timestamp = "Today at 07:30 AM IST",
            recommendedActions = listOf(
                "Do not enter ocean waters under any circumstance",
                "Remain strictly on the paved pedestrian promenade",
                "Follow Coastal Security Group lifeguards' instructions",
                "Keep children at least 30 meters back from breaking surge line"
            ),
            safeAlternative = "Visit Fort St. George Museum or San Thome Cathedral Basilica during high tide window."
        ),
        SafetyAlert(
            id = "alert_3",
            title = "Special Crowd Transit Plan — Madurai Temple East Gate",
            description = "Chithirai festive preparation and deity procession schedule will divert vehicular traffic away from East Avani Moola Street between 04:00 PM and 08:00 PM.",
            affectedArea = "Madurai Old Town & Meenakshi Outer Perimeter",
            severity = AlertSeverity.INFO,
            category = AlertCategory.ROUTE,
            source = "Madurai City Traffic Police",
            timestamp = "Yesterday at 09:00 PM IST",
            recommendedActions = listOf(
                "Use West Gate or South Gate pedestrian entrance queues",
                "Park four-wheelers at designated Corporation Multi-level Parking on West Veli Street",
                "Avail battery-operated transit shuttles for senior citizens"
            ),
            safeAlternative = "Arrive during the early morning darshan slot (06:00 AM - 08:30 AM) to avoid peak queue."
        ),
        SafetyAlert(
            id = "alert_4",
            title = "Munnar Gap Road Monsoon Rockfall Precautions",
            description = "Controlled geotechnical stabilization in progress along NH 85 Gap Road section. Single lane transit active.",
            affectedArea = "Munnar - Bodimettu Stretch, Idukki District",
            severity = AlertSeverity.CAUTION,
            category = AlertCategory.WEATHER,
            source = "Idukki District Disaster Management Authority (DDMA)",
            timestamp = "Verified 3 hours ago",
            recommendedActions = listOf(
                "Strictly follow pilot vehicle instructions at the check-post",
                "Avoid stopping vehicles under cut-slope overhangs",
                "Night vehicular movement prohibited between 09:00 PM and 05:00 AM"
            ),
            safeAlternative = "Alternate route via Poopara - Rajakkad is cleared and operating normally."
        )
    )

    val sampleTrips = listOf(
        Trip(
            id = "trip_1",
            title = "Madurai Heritage & Sacred Corridor",
            origin = "Madurai Junction Station",
            destination = "Meenakshi Amman Temple & Palace",
            travelMode = TravelMode.WALKING,
            date = "Oct 10, 2026",
            durationEst = "35 mins",
            distanceKm = 1.6,
            status = TripStatus.ACTIVE,
            stops = listOf("Madurai Junction", "Town Hall Road", "West Tower Entry", "Meenakshi Temple", "Thirumalai Nayakkar Mahal"),
            precautions = listOf(
                "Hydrate frequently in warm midday sun (carry 1L reusable water bottle)",
                "Deposit footwear at East Gate authorized stall",
                "Tourist Police Booth is active at Rayar Gopuram"
            ),
            checklist = listOf(
                TripChecklistItem("c1", "Offline map of Madurai old city downloaded", true),
                TripChecklistItem("c2", "Added local hospital (+91 452 258 0888) to contacts", true),
                TripChecklistItem("c3", "Shared live location with Trusted Contact (Rahul)", true),
                TripChecklistItem("c4", "Carried physical government photo ID (Aadhaar / Passport)", false),
                TripChecklistItem("c5", "Packed essential personal allergy medication", true)
            ),
            isSharingLocation = true
        ),
        Trip(
            id = "trip_2",
            title = "Nilgiri Scenic Hill Journey",
            origin = "Coimbatore Central",
            destination = "Ooty Lake & Botanical Gardens",
            travelMode = TravelMode.BUS,
            date = "Oct 15, 2026",
            durationEst = "3 hrs 15 mins",
            distanceKm = 88.0,
            status = TripStatus.UPCOMING,
            stops = listOf("Coimbatore New Bus Stand", "Mettupalayam Foothills", "Coonoor Hairpin View", "Ooty Bus Depot"),
            precautions = listOf(
                "Morning fog caution on hairpin bends 15-28",
                "Carry warm jacket; evening temperatures reach 10°C",
                "Pre-book toy train or government hill express bus seats"
            ),
            checklist = listOf(
                TripChecklistItem("c21", "Verified vehicle hill pass / e-pass requirement", false),
                TripChecklistItem("c22", "Motion sickness tablets packed in daypack", false),
                TripChecklistItem("c23", "Emergency contact numbers noted on offline card", false)
            ),
            isSharingLocation = false
        ),
        Trip(
            id = "trip_3",
            title = "Fort Kochi Coastal Heritage Walk",
            origin = "Ernakulam Boat Jetty",
            destination = "Fort Kochi Chinese Nets",
            travelMode = TravelMode.WALKING,
            date = "Sep 28, 2026",
            durationEst = "1 hr 10 mins",
            distanceKm = 3.2,
            status = TripStatus.COMPLETED,
            stops = listOf("Ernakulam Jetty", "Fort Kochi Terminal", "Princess Street", "St. Francis Church", "Chinese Fishing Nets"),
            precautions = listOf("Verified life-jacket provided on passenger ferry"),
            checklist = listOf(
                TripChecklistItem("c31", "Sun protection & umbrella packed", true),
                TripChecklistItem("c32", "Water metro card recharged", true)
            ),
            isSharingLocation = false
        )
    )

    val sampleEmergencyContacts = listOf(
        EmergencyContact(
            id = "cont_1",
            name = "Rahul Sharma",
            relationship = "Brother",
            phone = "+91 98401 23456",
            isPrimary = true,
            receivesLocationUpdates = true
        ),
        EmergencyContact(
            id = "cont_2",
            name = "Priya Ramanathan",
            relationship = "Friend / Travel Partner",
            phone = "+91 94440 98765",
            isPrimary = false,
            receivesLocationUpdates = true
        ),
        EmergencyContact(
            id = "cont_3",
            name = "Venkatesh Iyer",
            relationship = "Local Guide (Verified)",
            phone = "+91 98845 11223",
            isPrimary = false,
            receivesLocationUpdates = false
        )
    )

    val sampleEmergencyPhrases = listOf(
        PhraseItem(
            id = "p1",
            category = "Medical",
            english = "I need medical help immediately.",
            hindi = "मुझे तुरंत चिकित्सा सहायता की आवश्यकता है।",
            tamil = "எனக்கு உடனடியாக மருத்துவ உதவி தேவை.",
            pronunciation = "Enakku udanadiyaaga maruthuva udhavi thevai."
        ),
        PhraseItem(
            id = "p2",
            category = "Police",
            english = "Please call the police.",
            hindi = "कृपया पुलिस को बुलाएं।",
            tamil = "தயவுசெய்து காவல்துறையை அழையுங்கள்.",
            pronunciation = "Thayavuseidhu kaavalthuraiyai azhaiyungal."
        ),
        PhraseItem(
            id = "p3",
            category = "Lost",
            english = "I am lost. Where is the main road?",
            hindi = "मैं रास्ता भटक गया हूँ। मुख्य सड़क कहाँ है?",
            tamil = "நான் வழி தவறிவிட்டேன். முக்கிய சாலை எங்கே?",
            pronunciation = "Naan vazhi thavarivitten. Mukkiya saalai enge?"
        ),
        PhraseItem(
            id = "p4",
            category = "Medical",
            english = "Please call an ambulance (108).",
            hindi = "कृपया एम्बुलेंस (108) को फोन करें।",
            tamil = "தயவுசெய்து ஆம்புலன்ஸை (108) அழையுங்கள்.",
            pronunciation = "Thayavuseidhu ambulance-ai (108) azhaiyungal."
        ),
        PhraseItem(
            id = "p5",
            category = "Emergency",
            english = "Please contact my family.",
            hindi = "कृपया मेरे परिवार से संपर्क करें।",
            tamil = "தயவுசெய்து எனது குடும்பத்தை தொடர்பு கொள்ளுங்கள்.",
            pronunciation = "Thayavuseidhu enadhu kudumbathai thodarbu kollungal."
        ),
        PhraseItem(
            id = "p6",
            category = "Direction",
            english = "Where is the nearest hospital or pharmacy?",
            hindi = "निकटतम अस्पताल या फार्मेसी कहाँ है?",
            tamil = "அருகிலுள்ள மருத்துவமனை அல்லது மருந்தகம் எங்கே?",
            pronunciation = "Arugilulla maruthuvamanai allathu marundhagam enge?"
        ),
        PhraseItem(
            id = "p7",
            category = "Allergy",
            english = "I have a severe medicine allergy.",
            hindi = "मुझे दवा से गंभीर एलर्जी है।",
            tamil = "எனக்கு கடுமையான மருந்து ஒவ்வாமை (அலர்ஜி) உள்ளது.",
            pronunciation = "Enakku kadumaiyaana marundhu ovvaamai ullathu."
        )
    )

    // Official Indian Emergency Helplines
    val emergencyHelplines = listOf(
        HelplineInfo("112", "National Unified Emergency", "All Emergencies (Police, Fire, Medical, Rescue)"),
        HelplineInfo("108", "Emergency Ambulance Service", "Government Free Medical & Trauma Dispatch"),
        HelplineInfo("100", "State Police Dispatch", "Immediate Police Assistance"),
        HelplineInfo("1091", "Women's Safety Helpline", "Dedicated Women In Distress Support (24/7)"),
        HelplineInfo("1363", "Incredible India Tourist Helpline", "Multilingual 24/7 Tourist Guidance")
    )
}

data class HelplineInfo(
    val number: String,
    val title: String,
    val description: String
)
