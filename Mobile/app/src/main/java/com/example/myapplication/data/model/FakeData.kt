package com.example.myapplication.data.model

import com.example.myapplication.data.dto.UserDto

object PlayVideoScreenData {

    val author = UserDto(
        id = 1L,
        fullName = "Nguyễn Văn A",
        email = "nguyenvana@gmail.com",
        phoneNumber = "0123456789",
        username = "nguyenvana",
        imgUrl = "https://example.com/avatar.png",
        gender = "MALE",
        dateOfBirth = "2000-01-15"
    )


    val video = Video(
        id = 1,
        userId = 1,
        title = "Hướng dẫn làm app xem video",
        description = "Video hướng dẫn làm ứng dụng xem video tương tự YouTube bằng Kotlin",
        url = "huong-dan-lam-app-xem-video",
        urlThumbnail = "huong-dan-lam-app-xem-video",
        status = "published",
        visibility = "public",
    )

    val comment = Comment(
        id = null,
        userId = 1L,
        videoId = 3L,
        content = "Video hay quá bạn ơi!",
        createdAt = null,
        updatedAt = null
    )

    val comments = listOf(
        Comment(
            id = 1,
            userId = 1,
            videoId = 1,
            content = "Video hay quá bạn ơi!",
            createdAt = "2025-11-15T12:30:00",
            updatedAt = "2025-11-15T12:30:00"
        ), Comment(
            id = 2,
            userId = 2,
            videoId = 1,
            content = "Hướng dẫn rất dễ hiểu!",
            createdAt = "2025-11-15T12:35:00",
            updatedAt = "2025-11-15T12:35:00"
        ), Comment(
            id = 3,
            userId = 3,
            videoId = 1,
            content = "Cảm ơn bạn đã chia sẻ.",
            createdAt = "2025-11-15T12:40:00",
            updatedAt = "2025-11-15T12:40:00"
        ), Comment(
            id = 4,
            userId = 4,
            videoId = 1,
            content = "Mong bạn làm thêm nhiều video nữa!",
            createdAt = "2025-11-15T12:45:00",
            updatedAt = "2025-11-15T12:45:00"
        ), Comment(
            id = 5,
            userId = 5,
            videoId = 1,
            content = "Clip quá tuyệt!",
            createdAt = "2025-11-15T12:50:00",
            updatedAt = "2025-11-15T12:50:00"
        )
    )
}

object VideoData {
    val videoList = listOf(
        Video(
            id = 1,
            userId = 1,
            title = "Hướng dẫn làm app xem video bằng Kotlin",
            description = "Video hướng dẫn chi tiết cách làm app xem video như YouTube.",
            url = "huong-dan-app-xem-video",
            urlThumbnail = "huong-dan-lam-app-xem-video",
            status = "published",
            visibility = "public",
        ),
        Video(
            id = 2,
            userId = 1,
            title = "Top 10 video hot nhất tuần",
            description = "Tổng hợp những video nổi bật được xem nhiều nhất trong tuần này.",
            url = "top-10-video-hot-nhat-tuan",
            urlThumbnail = "huong-dan-lam-app-xem-video",
            status = "published",
            visibility = "public",
        ),
        Video(
            id = 3,
            userId = 1,
            title = "Cách tích hợp Firebase Storage vào app",
            description = "Hướng dẫn upload và phát video từ Firebase Storage.",
            url = "tich-hop-firebase-storage",
            urlThumbnail = "huong-dan-lam-app-xem-video",
            status = "published",
            visibility = "public",
        ),
        Video(
            id = 4,
            userId = 1,
            title = "Làm UI giống YouTube bằng Jetpack Compose",
            description = "Xây dựng giao diện YouTube hiện đại chỉ với vài dòng code.",
            url = "ui-giong-youtube-compose",
            urlThumbnail = "huong-dan-lam-app-xem-video",
            status = "published",
            visibility = "public",
        ),
        Video(
            id = 5,
            userId = 1,
            title = "Phát video từ Google Drive",
            description = "Hướng dẫn lấy video từ Google Drive và phát trong Android.",
            url = "phat-video-tu-google-drive",
            urlThumbnail = "huong-dan-lam-app-xem-video",
            status = "published",
            visibility = "public",
        ),
        Video(
            id = 6,
            userId = 1,
            title = "Sử dụng ExoPlayer trong Android",
            description = "Cách tích hợp ExoPlayer để phát video mượt mà, hỗ trợ nhiều định dạng.",
            url = "su-dung-exoplayer",
            urlThumbnail = "huong-dan-lam-app-xem-video",
            status = "published",
            visibility = "public",
        ),
        Video(
            id = 7,
            userId = 1,
            title = "Tối ưu tốc độ load video",
            description = "Các mẹo giúp video load nhanh và không bị giật lag.",
            url = "toi-uu-toc-do-load-video",
            urlThumbnail = "huong-dan-lam-app-xem-video",
            status = "published",
            visibility = "public",
        ),
        Video(
            id = 8,
            userId = 1,
            title = "Hiển thị thumbnail video bằng Glide",
            description = "Dùng Glide để hiển thị ảnh đại diện video nhanh chóng.",
            url = "thumbnail-video-glide",
            urlThumbnail = "huong-dan-lam-app-xem-video",
            status = "published",
            visibility = "public",
        ),
        Video(
            id = 9,
            userId = 1,
            title = "Tạo danh sách phát (Playlist)",
            description = "Cách nhóm video thành playlist giống YouTube.",
            url = "tao-playlist-video",
            urlThumbnail = "huong-dan-lam-app-xem-video",
            status = "published",
            visibility = "public",
        ),
        Video(
            id = 10,
            userId = 1,
            title = "Xử lý khi video bị lỗi mạng",
            description = "Hiển thị thông báo hoặc retry khi video không tải được.",
            url = "xu-ly-loi-video",
            urlThumbnail = "huong-dan-lam-app-xem-video",
            status = "published",
            visibility = "public",
        )
    )

    val video = Video(
        id = 1,
        userId = 1,
        title = "Hướng dẫn làm app xem video",
        description = "Video hướng dẫn làm ứng dụng xem video tương tự YouTube bằng Kotlin",
        url = "huong-dan-lam-app-xem-video",
        urlThumbnail = "huong-dan-lam-app-xem-video",
        status = "published",
        visibility = "public",
    )

}

object UserDtoData {
    val userDto = UserDto(
        id = 1L, // có thể bỏ nếu muốn để null
        fullName = "Nguyễn Văn A",
        email = "nguyenvana@example.com",
        phoneNumber = "0123456789",
        username = "nguyenvana",
        imgUrl = "https://example.com/avatar.jpg", // có thể bỏ = null
        gender = "Male",
        dateOfBirth = "1990-01-01"
    )
}

object UserPlaylistData {

    val samplePlaylists = listOf(
        UserPlaylist(
            1,
            1,
            "Video đã thích",
            "Danh sách video đã thích của người dùng",
            "PRIVATE",
            System.currentTimeMillis()
        ),
        UserPlaylist(
            2,
            1,
            "Lập trình Android",
            "Tổng hợp video hướng dẫn Android Kotlin",
            "PUBLIC",
            System.currentTimeMillis()
        ),
        UserPlaylist(
            3,
            1,
            "Nhạc Lofi",
            "Playlist nhạc chill nghe khi làm việc",
            "PUBLIC",
            System.currentTimeMillis()
        ),
        UserPlaylist(
            4,
            1,
            "Học Spring Boot",
            "Video backend, Spring Security, JPA",
            "UNLISTED",
            System.currentTimeMillis()
        ),
        UserPlaylist(5, 1,"Xem sau", "Videos lưu để xem sau", "PRIVATE", System.currentTimeMillis()),

        UserPlaylist(
            6,
            1,
            "Công thức nấu ăn",
            "Các món ăn dễ làm tại nhà",
            "PUBLIC",
            System.currentTimeMillis()
        ),
        UserPlaylist(
            7,
            1,
            "Travel Vlog",
            "Tổng hợp các video du lịch yêu thích",
            "PUBLIC",
            System.currentTimeMillis()
        ),
        UserPlaylist(
            8,
            1,
            "Nhạc Gym",
            "Playlist nghe khi tập luyện",
            "PUBLIC",
            System.currentTimeMillis()
        ),
        UserPlaylist(
            9,
            1,
            "Học SEO",
            "Các video hướng dẫn SEO & Marketing",
            "UNLISTED",
            System.currentTimeMillis()
        ),
        UserPlaylist(
            10,
            1,
            "Video đã thích",
            "Danh sách video đã thích",
            "PRIVATE",
            System.currentTimeMillis()
        )
    )
}