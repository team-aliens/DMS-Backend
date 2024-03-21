package team.aliens.dms.thirdparty.notification

// @Configuration
// class FCMConfig(
//    @Value("\${fcm.file-url}")
//    private val url: String
// ) {
//
//    @PostConstruct
//    fun initialize() {
//        try {
//            URL(url).openStream().use { inputStream ->
//                Files.copy(inputStream, Paths.get(PATH))
//                val file = File(PATH)
//                if (FirebaseApp.getApps().isEmpty()) {
//                    val options = FirebaseOptions.builder()
//                        .setCredentials(GoogleCredentials.fromStream(file.inputStream()))
//                        .build()
//                    FirebaseApp.initializeApp(options)
//                }
//                file.delete()
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    companion object {
//        private const val PATH = "./credentials.json"
//    }
// }
