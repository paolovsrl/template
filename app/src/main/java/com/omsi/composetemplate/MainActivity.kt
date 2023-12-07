package com.omsi.composetemplate

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.omsi.composetemplate.data.Navigator
import com.omsi.composetemplate.data.checkPermissions
import com.omsi.composetemplate.ui.screens.MainScreen
import com.omsi.composetemplate.ui.screens.WaitingPage
import com.omsi.composetemplate.ui.theme.ComposeTemplateTheme
import com.omsi.marmix.ui.remote.ui.utils.ActivityEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        super.onCreate(savedInstanceState)
        //For enabling autostart
        checkPermissions(this)


        val insetsController = WindowCompat.getInsetsController(window, window.decorView)


        insetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val navController = rememberNavController()
            ComposeTemplateTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = isSystemInDarkTheme()
                SideEffect {
                    // Update all of the system bar colors to be transparent, and use
                    // dark icons if we're in light theme
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                    // setStatusBarColor() and setNavigationBarColor() also exist
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val insets = WindowInsets.systemBars.union(WindowInsets.ime)
                    val topInset =  WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
                    Scaffold(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .imePadding()
                        //.systemBarsPadding()
                        ,
                        topBar = {
                            TopAppBar(
                                title = {Text(getString(R.string.app_name))},
                                colors= TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    titleContentColor = MaterialTheme.colorScheme.onPrimary),
                                windowInsets = insets
                            )}
                    ){
                        Box(Modifier.fillMaxSize().padding(it)) {
                            NavigationComponent(navController = navController, navigator = navigator)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun NavigationComponent(navController: NavHostController, navigator: Navigator){
    LaunchedEffect(key1 = "navigation", block ={
        navigator.sharedFlow.onEach {
            if (it == Navigator.NavTarget.Waiting) {
                navController.popBackStack(it.label, false)
            } else
                navController.navigate(it.label) {
                    launchSingleTop = true
                }
            navigator.currentPage=it
        }.launchIn(this)
    })

    val mainViewModel = hiltViewModel<MainViewModel>()
    val activity = (LocalContext.current as Activity)

    ActivityEventListener(){event->
        when(event){
            Lifecycle.Event.ON_RESUME->{
                mainViewModel.onStart(activity)
            }
            Lifecycle.Event.ON_PAUSE ->{
                mainViewModel.onStop()
            }
            else ->{

            }
        }
    }

    NavHost(navController = navController, startDestination = navigator.initialPage.label){
        composable(Navigator.NavTarget.MainPage.label){

            BackHandler(true) {
                navigator.navigateTo(Navigator.NavTarget.Waiting)
            }

            MainScreen(canEnabled = mainViewModel.canInitialized)

        }

        composable(Navigator.NavTarget.Waiting.label){
            WaitingPage()
        }
    }
}