package com.virtualsblog.project.data.mapper

// Import ini dimungkinkan karena dependensi "testImplementation(libs.truth)" di gradle
import com.google.common.truth.Truth.assertThat
// Import-import ini sesuai dengan struktur paket (namespace) proyek Anda
import com.virtualsblog.project.data.local.entities.UserEntity
import com.virtualsblog.project.domain.model.User
// Import ini dimungkinkan karena dependensi "testImplementation(libs.junit)" di gradle
import org.junit.Test

class UserMapperTest {

    // Anotasi @Test dari JUnit, menandakan ini adalah sebuah fungsi tes.
    @Test
    fun `mapEntityToDomain - mengonversi UserEntity menjadi User domain model dengan benar`() {
        // 1. Arrange (Persiapan)
        // Membuat data input tiruan. Ini tidak memerlukan dependensi eksternal.
        val testUserEntity = UserEntity(
            id = "user-123",
            username = "lisvindanu",
            fullname = "Lisvindanu",
            email = "lisvindanu@example.com",
            image = "url/to/image.jpg",
            createdAt = "2025-06-11T23:00:00Z",
            updatedAt = "2025-06-11T23:00:00Z",
            isCurrent = true
        )

        // 2. Act (Tindakan)
        // Memanggil fungsi yang ingin diuji.
        val resultUserDomain: User = UserMapper.mapEntityToDomain(testUserEntity)

        // 3. Assert (Verifikasi)
        // Menggunakan "assertThat" dari Google Truth untuk membandingkan hasil.
        assertThat(resultUserDomain.id).isEqualTo(testUserEntity.id)
        assertThat(resultUserDomain.username).isEqualTo(testUserEntity.username)
        assertThat(resultUserDomain.fullname).isEqualTo(testUserEntity.fullname)
        assertThat(resultUserDomain.email).isEqualTo(testUserEntity.email)
        assertThat(resultUserDomain.image).isEqualTo(testUserEntity.image)
    }
}