package com.virtualsblog.project

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.virtualsblog.project.data.local.dao.UserDao
import com.virtualsblog.project.domain.model.Post
import com.virtualsblog.project.domain.repository.AuthRepository
import com.virtualsblog.project.domain.usecase.blog.GetPostsForHomeUseCase
import com.virtualsblog.project.domain.usecase.blog.GetTotalPostsCountUseCase
import com.virtualsblog.project.domain.usecase.blog.ToggleLikeUseCase
import com.virtualsblog.project.presentation.ui.screen.home.HomeViewModel
import com.virtualsblog.project.presentation.ui.screen.home.HomeScreen
import com.virtualsblog.project.presentation.ui.theme.VirtualblogTheme
import com.virtualsblog.project.util.NavigationState
import com.virtualsblog.project.util.Resource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SimpleHomeScreenEspressoTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockAuthRepository: AuthRepository = mockk(relaxed = true)
    private val mockUserDao: UserDao = mockk(relaxed = true)
    private val mockGetPostsForHomeUseCase: GetPostsForHomeUseCase = mockk(relaxed = true)
    private val mockGetTotalPostsCountUseCase: GetTotalPostsCountUseCase = mockk(relaxed = true)
    private val mockToggleLikeUseCase: ToggleLikeUseCase = mockk(relaxed = true)
    private val mockNavigationState: NavigationState = mockk(relaxed = true)

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        // Mock untuk fungsi non-suspend
        every { mockAuthRepository.getCurrentUser() } returns flowOf(null)
        every { mockUserDao.getCurrentUser() } returns flowOf(null)
        every { mockNavigationState.shouldRefreshHome } returns false

        // ✨ FIX: Gunakan coEvery untuk suspend function
        coEvery { mockGetTotalPostsCountUseCase() } returns flowOf(Resource.Success(0))
    }

    @Test
    fun homeScreen_showsLoadingState() {
        // Given: ✨ FIX: Gunakan coEvery untuk suspend function
        coEvery { mockGetPostsForHomeUseCase() } returns flowOf(Resource.Loading())

        // When
        viewModel = HomeViewModel(
            authRepository = mockAuthRepository,
            userDao = mockUserDao,
            getPostsForHomeUseCase = mockGetPostsForHomeUseCase,
            getTotalPostsCountUseCase = mockGetTotalPostsCountUseCase,
            toggleLikeUseCase = mockToggleLikeUseCase,
            navigationState = mockNavigationState
        )

        composeTestRule.setContent {
            VirtualblogTheme {
                HomeScreen(viewModel = viewModel, onNavigateToProfile = {}, onNavigateToCreatePost = {}, onNavigateToPostDetail = {}, onNavigateToLogin = {}, onNavigateToAllPosts = {}, onNavigateToCategories = {}, onNavigateToSearch = {})
            }
        }

        // Then
        composeTestRule.onNodeWithText("Memuat postingan...").assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysPostsWhenAvailable() {
        // Given
        val testPosts = listOf(
            Post(id = "post-1", title = "Judul Postingan Percobaan", content = "Konten...", author = "Penulis Uji", authorId = "author-1", authorUsername = "testauthor", category = "Teknologi", categoryId = "cat-1", createdAt = "2025-01-01T00:00:00Z", updatedAt = "2025-01-01T00:00:00Z", likes = 10, comments = 2, isLiked = false, slug = "slug-1")
        )
        // ✨ FIX: Gunakan coEvery untuk suspend function
        coEvery { mockGetPostsForHomeUseCase() } returns flowOf(Resource.Success(testPosts))

        // When
        viewModel = HomeViewModel(
            authRepository = mockAuthRepository,
            userDao = mockUserDao,
            getPostsForHomeUseCase = mockGetPostsForHomeUseCase,
            getTotalPostsCountUseCase = mockGetTotalPostsCountUseCase,
            toggleLikeUseCase = mockToggleLikeUseCase,
            navigationState = mockNavigationState
        )

        composeTestRule.setContent {
            VirtualblogTheme {
                HomeScreen(viewModel = viewModel, onNavigateToProfile = {}, onNavigateToCreatePost = {}, onNavigateToPostDetail = {}, onNavigateToLogin = {}, onNavigateToAllPosts = {}, onNavigateToCategories = {}, onNavigateToSearch = {})
            }
        }

        // Then
        composeTestRule.onNodeWithText("Judul Postingan Percobaan").assertIsDisplayed()
        composeTestRule.onNodeWithText("Penulis Uji").assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsEmptyState() {
        // Given: ✨ FIX: Gunakan coEvery untuk suspend function
        coEvery { mockGetPostsForHomeUseCase() } returns flowOf(Resource.Success(emptyList()))

        // When
        viewModel = HomeViewModel(
            authRepository = mockAuthRepository,
            userDao = mockUserDao,
            getPostsForHomeUseCase = mockGetPostsForHomeUseCase,
            getTotalPostsCountUseCase = mockGetTotalPostsCountUseCase,
            toggleLikeUseCase = mockToggleLikeUseCase,
            navigationState = mockNavigationState
        )

        composeTestRule.setContent {
            VirtualblogTheme {
                HomeScreen(viewModel = viewModel, onNavigateToProfile = {}, onNavigateToCreatePost = {}, onNavigateToPostDetail = {}, onNavigateToLogin = {}, onNavigateToAllPosts = {}, onNavigateToCategories = {}, onNavigateToSearch = {})
            }
        }

        // Then
        composeTestRule.onNodeWithText("Belum Ada Postingan").assertIsDisplayed()
    }
}