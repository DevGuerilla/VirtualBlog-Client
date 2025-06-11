package com.virtualsblog.project.domain.usecase.blog

import com.google.common.truth.Truth.assertThat
import com.virtualsblog.project.domain.model.Post
import com.virtualsblog.project.domain.repository.BlogRepository
import com.virtualsblog.project.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPostsForHomeUseCaseTest {

    // Dependensi yang akan kita mock
    private lateinit var mockBlogRepository: BlogRepository
    // Kelas yang akan kita uji
    private lateinit var getPostsForHomeUseCase: GetPostsForHomeUseCase

    // @Before dijalankan sebelum setiap @Test
    @Before
    fun setUp() {
        // Membuat mock untuk repository
        mockBlogRepository = mockk()
        // Membuat instance use case dengan mock repository
        getPostsForHomeUseCase = GetPostsForHomeUseCase(mockBlogRepository)
    }

    // `runTest` adalah cara standar untuk menjalankan kode coroutine di dalam unit test
    @Test
    fun `invoke - saat repository sukses, use case mengembalikan data yang benar`() = runTest {
        // 1. Arrange (Persiapan)
        // Buat data palsu yang akan dikembalikan oleh mock
        val fakePosts = listOf(
            Post(id = "post-1", title = "Postingan Uji Coba", content = "", author = "", authorId = "", authorUsername = "", category = "", categoryId = "", createdAt = "", updatedAt = "", likes = 0, comments = 0, isLiked = false, slug = "")
        )
        // Siapkan mock repository. Gunakan `coEvery` karena `getPostsForHome` adalah `suspend function`
        coEvery { mockBlogRepository.getPostsForHome() } returns flowOf(Resource.Success(fakePosts))

        // 2. Act (Tindakan)
        // Panggil fungsi `invoke()` dari use case
        val result = getPostsForHomeUseCase()

        // 3. Assert (Verifikasi)
        // Ambil item pertama dari Flow dan periksa isinya
        val resource = result.first()
        assertThat(resource).isInstanceOf(Resource.Success::class.java)
        assertThat((resource as Resource.Success).data).isEqualTo(fakePosts)
    }

    @Test
    fun `invoke - saat repository error, use case mengembalikan pesan error`() = runTest {
        // 1. Arrange
        val errorMessage = "Koneksi Gagal"
        // Siapkan mock repository untuk mengembalikan error
        coEvery { mockBlogRepository.getPostsForHome() } returns flowOf(Resource.Error(errorMessage))

        // 2. Act
        val result = getPostsForHomeUseCase()

        // 3. Assert
        val resource = result.first()
        assertThat(resource).isInstanceOf(Resource.Error::class.java)
        assertThat((resource as Resource.Error).message).isEqualTo(errorMessage)
    }
}