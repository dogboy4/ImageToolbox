package ru.tech.imageresizershrinker.presentation.filters_screen

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Colorize
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.Compare
import androidx.compose.material.icons.rounded.FileOpen
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.PhotoFilter
import androidx.compose.material.icons.rounded.Texture
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.ZoomIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.t8rin.dynamic.theme.LocalDynamicThemeState
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.tech.imageresizershrinker.R
import ru.tech.imageresizershrinker.presentation.draw_screen.components.PickColorFromImageSheet
import ru.tech.imageresizershrinker.presentation.filters_screen.components.AddEditMaskSheet
import ru.tech.imageresizershrinker.presentation.filters_screen.components.AddFilterButton
import ru.tech.imageresizershrinker.presentation.filters_screen.components.AddFiltersSheet
import ru.tech.imageresizershrinker.presentation.filters_screen.components.FilterItem
import ru.tech.imageresizershrinker.presentation.filters_screen.components.FilterReorderSheet
import ru.tech.imageresizershrinker.presentation.filters_screen.components.MaskItem
import ru.tech.imageresizershrinker.presentation.filters_screen.components.MaskReorderSheet
import ru.tech.imageresizershrinker.presentation.filters_screen.viewModel.FilterViewModel
import ru.tech.imageresizershrinker.presentation.root.theme.mixedContainer
import ru.tech.imageresizershrinker.presentation.root.theme.onMixedContainer
import ru.tech.imageresizershrinker.presentation.root.transformation.ImageInfoTransformation
import ru.tech.imageresizershrinker.presentation.root.utils.confetti.LocalConfettiController
import ru.tech.imageresizershrinker.presentation.root.utils.helper.Picker
import ru.tech.imageresizershrinker.presentation.root.utils.helper.failedToSaveImages
import ru.tech.imageresizershrinker.presentation.root.utils.helper.localImagePickerMode
import ru.tech.imageresizershrinker.presentation.root.utils.helper.parseSaveResult
import ru.tech.imageresizershrinker.presentation.root.utils.helper.rememberImagePicker
import ru.tech.imageresizershrinker.presentation.root.utils.navigation.Screen
import ru.tech.imageresizershrinker.presentation.root.widget.buttons.BottomButtonsBlock
import ru.tech.imageresizershrinker.presentation.root.widget.controls.EnhancedButton
import ru.tech.imageresizershrinker.presentation.root.widget.controls.EnhancedIconButton
import ru.tech.imageresizershrinker.presentation.root.widget.controls.ExtensionGroup
import ru.tech.imageresizershrinker.presentation.root.widget.controls.QualityWidget
import ru.tech.imageresizershrinker.presentation.root.widget.controls.SaveExifWidget
import ru.tech.imageresizershrinker.presentation.root.widget.dialogs.ExitWithoutSavingDialog
import ru.tech.imageresizershrinker.presentation.root.widget.image.ImageContainer
import ru.tech.imageresizershrinker.presentation.root.widget.image.ImageCounter
import ru.tech.imageresizershrinker.presentation.root.widget.image.imageStickyHeader
import ru.tech.imageresizershrinker.presentation.root.widget.modifier.container
import ru.tech.imageresizershrinker.presentation.root.widget.modifier.containerFabBorder
import ru.tech.imageresizershrinker.presentation.root.widget.modifier.drawHorizontalStroke
import ru.tech.imageresizershrinker.presentation.root.widget.modifier.navBarsLandscapePadding
import ru.tech.imageresizershrinker.presentation.root.widget.other.LoadingDialog
import ru.tech.imageresizershrinker.presentation.root.widget.other.LocalToastHost
import ru.tech.imageresizershrinker.presentation.root.widget.other.TopAppBarEmoji
import ru.tech.imageresizershrinker.presentation.root.widget.other.showError
import ru.tech.imageresizershrinker.presentation.root.widget.preferences.PreferenceItem
import ru.tech.imageresizershrinker.presentation.root.widget.preferences.screens.BasicFilterPreference
import ru.tech.imageresizershrinker.presentation.root.widget.preferences.screens.MaskFilterPreference
import ru.tech.imageresizershrinker.presentation.root.widget.sheets.CompareSheet
import ru.tech.imageresizershrinker.presentation.root.widget.sheets.PickImageFromUrisSheet
import ru.tech.imageresizershrinker.presentation.root.widget.sheets.SimpleSheet
import ru.tech.imageresizershrinker.presentation.root.widget.sheets.ZoomModalSheet
import ru.tech.imageresizershrinker.presentation.root.widget.text.TitleItem
import ru.tech.imageresizershrinker.presentation.root.widget.text.TopAppBarTitle
import ru.tech.imageresizershrinker.presentation.root.widget.utils.LocalSettingsState
import ru.tech.imageresizershrinker.presentation.root.widget.utils.LocalWindowSizeClass
import ru.tech.imageresizershrinker.presentation.root.widget.utils.isExpanded
import ru.tech.imageresizershrinker.presentation.root.widget.utils.middleImageState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(
    type: Screen.Filter.Type?,
    onGoBack: () -> Unit,
    viewModel: FilterViewModel = hiltViewModel()
) {
    val settingsState = LocalSettingsState.current

    val context = LocalContext.current as ComponentActivity
    val toastHostState = LocalToastHost.current
    val themeState = LocalDynamicThemeState.current
    val allowChangeColor = settingsState.allowChangeColorByImage

    val scope = rememberCoroutineScope()
    val confettiController = LocalConfettiController.current
    val showConfetti: () -> Unit = {
        scope.launch {
            confettiController.showEmpty()
        }
    }

    LaunchedEffect(type) {
        type?.let { viewModel.setType(it) }
    }

    LaunchedEffect(viewModel.previewBitmap) {
        viewModel.previewBitmap?.let {
            if (allowChangeColor) {
                themeState.updateColorByImage(it)
            }
        }
    }

    val pickImagesLauncher =
        rememberImagePicker(
            mode = localImagePickerMode(Picker.Multiple)
        ) { list ->
            list.takeIf { it.isNotEmpty() }?.let(viewModel::setBasicFilter)
        }

    val pickSingleImageLauncher = rememberImagePicker(
        mode = localImagePickerMode(Picker.Single)
    ) { list ->
        list.takeIf { it.isNotEmpty() }?.firstOrNull()?.let(viewModel::setMaskFilter)
    }

    val showAddMaskSheet = rememberSaveable { mutableStateOf(false) }

    var showExitDialog by rememberSaveable { mutableStateOf(false) }
    val showAddFilterSheet = rememberSaveable { mutableStateOf(false) }

    val onBack = {
        if (viewModel.canSave) showExitDialog = true
        else if (viewModel.filterType != null) {
            viewModel.clearType()
        } else onGoBack()
    }

    val showSheet = rememberSaveable { mutableStateOf(false) }
    val zoomButton = @Composable {
        AnimatedVisibility(
            visible = viewModel.bitmap != null,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            IconButton(
                onClick = {
                    showSheet.value = true
                }
            ) {
                Icon(Icons.Rounded.ZoomIn, null)
            }
        }
    }

    val showCompareSheet = rememberSaveable { mutableStateOf(false) }
    val compareButton = @Composable {
        AnimatedVisibility(
            visible = viewModel.previewBitmap != null,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            IconButton(
                onClick = {
                    showCompareSheet.value = true
                }
            ) {
                Icon(Icons.Rounded.Compare, null)
            }
        }
    }

    val imageInside =
        LocalConfiguration.current.orientation != Configuration.ORIENTATION_LANDSCAPE || LocalWindowSizeClass.current.widthSizeClass == WindowWidthSizeClass.Compact

    val interactionSource = remember { MutableInteractionSource() }

    val focus = LocalFocusManager.current
    val showPickImageFromUrisSheet = rememberSaveable { mutableStateOf(false) }

    var showOriginal by remember { mutableStateOf(false) }
    var imageState by remember { mutableStateOf(middleImageState()) }
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = topAppBarState, canScroll = { !imageState.isExpanded() && !showOriginal }
    )

    LaunchedEffect(imageState, showOriginal) {
        if (imageState.isExpanded() || showOriginal) {
            while (topAppBarState.heightOffset > topAppBarState.heightOffsetLimit) {
                topAppBarState.heightOffset -= 5f
                delay(1)
            }
        }
    }

    val showReorderSheet = rememberSaveable { mutableStateOf(false) }
    val actions: @Composable RowScope.() -> Unit = {
        Spacer(modifier = Modifier.width(8.dp))
        if (viewModel.bitmap != null) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .indication(
                        interactionSource,
                        LocalIndication.current
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                val press = PressInteraction.Press(it)
                                interactionSource.emit(press)
                                if (viewModel.canShow()) {
                                    showOriginal = true
                                }
                                tryAwaitRelease()
                                showOriginal = false
                                interactionSource.emit(
                                    PressInteraction.Release(
                                        press
                                    )
                                )
                            }
                        )
                    }
            ) {
                Icon(
                    Icons.Rounded.History,
                    null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp)
                )
            }
        }
        compareButton()
        zoomButton()
        if (viewModel.bitmap != null && (viewModel.basicFilterState.filters.size >= 2 || viewModel.maskingFilterState.masks.size >= 2)) {
            IconButton(onClick = { showReorderSheet.value = true }) {
                Icon(Icons.Rounded.Tune, null)
            }
        }
    }

    val imageBlock = @Composable {
        ImageContainer(
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(
                    onTap = { showPickImageFromUrisSheet.value = true }
                )
            },
            imageInside = imageInside,
            showOriginal = showOriginal,
            previewBitmap = viewModel.previewBitmap,
            originalBitmap = viewModel.bitmap,
            isLoading = viewModel.isImageLoading,
            shouldShowPreview = true,
            animatePreviewChange = false
        )
    }

    val buttons: @Composable (filterType: Screen.Filter.Type) -> Unit = { filterType ->
        BottomButtonsBlock(
            targetState = (viewModel.basicFilterState.uris.isNullOrEmpty() && viewModel.maskingFilterState.uri == null) to imageInside,
            onPickImage = {
                when (filterType) {
                    is Screen.Filter.Type.Basic -> pickImagesLauncher.pickImage()
                    is Screen.Filter.Type.Masking -> pickSingleImageLauncher.pickImage()
                }
            },
            onSaveBitmap = {
                when (filterType) {
                    is Screen.Filter.Type.Basic -> {
                        viewModel.saveBitmaps { failed, savingPath ->
                            context.failedToSaveImages(
                                scope = scope,
                                failed = failed,
                                done = viewModel.done,
                                toastHostState = toastHostState,
                                savingPathString = savingPath,
                                showConfetti = showConfetti
                            )
                        }
                    }

                    is Screen.Filter.Type.Masking -> {
                        viewModel.saveMaskedBitmap { saveResult ->
                            parseSaveResult(
                                saveResult = saveResult,
                                onSuccess = showConfetti,
                                toastHostState = toastHostState,
                                scope = scope,
                                context = context
                            )
                        }
                    }
                }
            },
            canSave = viewModel.canSave,
            columnarFab = {
                FloatingActionButton(
                    onClick = {
                        when (filterType) {
                            is Screen.Filter.Type.Basic -> showAddFilterSheet.value = true
                            is Screen.Filter.Type.Masking -> showAddMaskSheet.value = true
                        }
                    },
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                    modifier = Modifier.containerFabBorder(),
                    containerColor = MaterialTheme.colorScheme.mixedContainer,
                    contentColor = MaterialTheme.colorScheme.onMixedContainer
                ) {
                    when (filterType) {
                        is Screen.Filter.Type.Basic -> {
                            Icon(Icons.Rounded.PhotoFilter, null)
                        }

                        is Screen.Filter.Type.Masking -> {
                            Icon(Icons.Rounded.Texture, null)
                        }
                    }
                }

            },
            actions = {
                if (imageInside) actions()
            }
        )
    }

    val controls: @Composable (filterType: Screen.Filter.Type) -> Unit = { filterType ->
        val baseControls: @Composable (wrapped: @Composable () -> Unit) -> Unit = { wrapped ->
            if (viewModel.bitmap != null) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        bottom = WindowInsets
                            .navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding() + WindowInsets.ime
                            .asPaddingValues()
                            .calculateBottomPadding() + (if (!imageInside && viewModel.bitmap != null) 20.dp else 100.dp),
                        top = if (viewModel.bitmap == null || !imageInside) 20.dp else 0.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                    modifier = Modifier
                        .fillMaxHeight(1f)
                        .clipToBounds()
                ) {
                    imageStickyHeader(
                        visible = imageInside && viewModel.bitmap != null,
                        expanded = showOriginal,
                        imageState = imageState,
                        onStateChange = { imageState = it },
                        imageBlock = imageBlock
                    )
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .navBarsLandscapePadding(viewModel.bitmap == null),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            wrapped()
                        }
                    }
                }
            }
        }

        when (filterType) {
            is Screen.Filter.Type.Basic -> {
                baseControls {
                    val filterList = viewModel.basicFilterState.filters
                    if (imageInside && viewModel.bitmap == null) imageBlock()
                    if (viewModel.bitmap != null) {
                        ImageCounter(
                            imageCount = viewModel.basicFilterState.uris?.size?.takeIf { it > 1 },
                            onRepick = {
                                showPickImageFromUrisSheet.value = true
                            }
                        )
                        AnimatedContent(
                            targetState = filterList.isNotEmpty(),
                            transitionSpec = {
                                fadeIn() + expandVertically() togetherWith fadeOut() + shrinkVertically()
                            }
                        ) { notEmpty ->
                            if (notEmpty) {
                                Column(Modifier.container(MaterialTheme.shapes.extraLarge)) {
                                    TitleItem(text = stringResource(R.string.filters))
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(
                                            8.dp
                                        ),
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        filterList.forEachIndexed { index, filter ->
                                            FilterItem(
                                                filter = filter,
                                                onFilterChange = {
                                                    viewModel.updateFilter(
                                                        value = it,
                                                        index = index,
                                                        showError = {
                                                            scope.launch {
                                                                toastHostState.showError(
                                                                    context,
                                                                    it
                                                                )
                                                            }
                                                        }
                                                    )
                                                },
                                                onLongPress = {
                                                    showReorderSheet.value =
                                                        true
                                                },
                                                showDragHandle = false,
                                                onRemove = {
                                                    viewModel.removeFilterAtIndex(
                                                        index
                                                    )
                                                }
                                            )
                                        }
                                        AddFilterButton(
                                            onClick = {
                                                showAddFilterSheet.value = true
                                            },
                                            modifier = Modifier.padding(
                                                horizontal = 16.dp
                                            )
                                        )
                                    }
                                }
                            } else {
                                AddFilterButton(
                                    onClick = {
                                        showAddFilterSheet.value = true
                                    },
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp
                                    )
                                )
                            }
                        }
                        Spacer(Modifier.size(8.dp))
                        SaveExifWidget(
                            imageFormat = viewModel.imageInfo.imageFormat,
                            selected = viewModel.keepExif,
                            onCheckedChange = {
                                viewModel.setKeepExif(
                                    !viewModel.keepExif
                                )
                            }
                        )
                        if (viewModel.imageInfo.imageFormat.canChangeCompressionValue) Spacer(
                            Modifier.size(8.dp)
                        )
                        QualityWidget(
                            imageFormat = viewModel.imageInfo.imageFormat,
                            enabled = viewModel.bitmap != null,
                            quality = viewModel.imageInfo.quality,
                            onQualityChange = viewModel::setQuality
                        )
                        Spacer(Modifier.size(8.dp))
                        ExtensionGroup(
                            enabled = viewModel.bitmap != null,
                            imageFormat = viewModel.imageInfo.imageFormat,
                            onFormatChange = {
                                viewModel.setImageFormat(it)
                            }
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }

            is Screen.Filter.Type.Masking -> {
                baseControls {
                    val maskList = viewModel.maskingFilterState.masks
                    if (imageInside && viewModel.bitmap == null) imageBlock()
                    if (viewModel.bitmap != null) {
                        AnimatedContent(
                            targetState = maskList.isNotEmpty(),
                            transitionSpec = {
                                fadeIn() + expandVertically() togetherWith fadeOut() + shrinkVertically()
                            }
                        ) { notEmpty ->
                            if (notEmpty) {
                                Column(Modifier.container(MaterialTheme.shapes.extraLarge)) {
                                    TitleItem(text = stringResource(R.string.masks))
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(
                                            4.dp
                                        ),
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        maskList.forEachIndexed { index, mask ->
                                            MaskItem(
                                                imageUri = viewModel.maskingFilterState.uri,
                                                previousMasks = maskList.take(index),
                                                imageManager = viewModel.getImageManager(),
                                                mask = mask,
                                                titleText = stringResource(
                                                    R.string.mask_indexed,
                                                    index + 1
                                                ),
                                                onMaskChange = {
                                                    viewModel.updateMask(
                                                        value = it,
                                                        index = index,
                                                        showError = {
                                                            scope.launch {
                                                                toastHostState.showError(
                                                                    context,
                                                                    it
                                                                )
                                                            }
                                                        }
                                                    )
                                                },
                                                onLongPress = {
                                                    showReorderSheet.value = true
                                                },
                                                showDragHandle = false,
                                                onRemove = {
                                                    viewModel.removeMaskAtIndex(index)
                                                }
                                            )
                                        }
                                        EnhancedButton(
                                            containerColor = MaterialTheme.colorScheme.mixedContainer,
                                            onClick = {
                                                showAddMaskSheet.value = true
                                            },
                                            modifier = Modifier.padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                                top = 4.dp
                                            )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.Texture,
                                                contentDescription = null
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Text(stringResource(id = R.string.add_mask))
                                        }
                                    }
                                }
                            } else {
                                EnhancedButton(
                                    containerColor = MaterialTheme.colorScheme.mixedContainer,
                                    onClick = {
                                        showAddMaskSheet.value = true
                                    },
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Texture,
                                        contentDescription = null
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(stringResource(id = R.string.add_mask))
                                }
                            }
                        }

                        Spacer(Modifier.size(8.dp))
                        SaveExifWidget(
                            imageFormat = viewModel.imageInfo.imageFormat,
                            selected = viewModel.keepExif,
                            onCheckedChange = {
                                viewModel.setKeepExif(
                                    !viewModel.keepExif
                                )
                            }
                        )
                        if (viewModel.imageInfo.imageFormat.canChangeCompressionValue) Spacer(
                            Modifier.size(8.dp)
                        )
                        QualityWidget(
                            imageFormat = viewModel.imageInfo.imageFormat,
                            enabled = viewModel.bitmap != null,
                            quality = viewModel.imageInfo.quality,
                            onQualityChange = viewModel::setQuality
                        )
                        Spacer(Modifier.size(8.dp))
                        ExtensionGroup(
                            enabled = viewModel.bitmap != null,
                            imageFormat = viewModel.imageInfo.imageFormat,
                            onFormatChange = {
                                viewModel.setImageFormat(it)
                            }
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }

    val content: @Composable BoxScope.(filterType: Screen.Filter.Type) -> Unit = { filterType ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (!imageInside) {
                Box(
                    Modifier
                        .container(
                            shape = RectangleShape,
                            color = MaterialTheme.colorScheme.surfaceContainerLow
                        )
                        .fillMaxHeight()
                        .weight(1.2f)
                        .padding(20.dp)
                ) {
                    Box(Modifier.align(Alignment.Center)) {
                        imageBlock()
                    }
                }
            }

            Box(Modifier.weight(1f)) {
                controls(filterType)
            }

            if (!imageInside && viewModel.bitmap != null) {
                buttons(filterType)
            }
        }

        if (imageInside || viewModel.bitmap == null) {
            Box(
                modifier = Modifier.align(settingsState.fabAlignment)
            ) {
                buttons(filterType)
            }
        }
    }

    val showColorPicker = remember { mutableStateOf(false) }
    var tempColor by remember { mutableStateOf(Color.Black) }

    PickColorFromImageSheet(
        visible = showColorPicker,
        bitmap = viewModel.previewBitmap,
        onColorChange = { tempColor = it },
        color = tempColor
    )

    ZoomModalSheet(
        bitmap = viewModel.previewBitmap,
        visible = showSheet
    )

    CompareSheet(
        data = viewModel.bitmap to viewModel.previewBitmap,
        visible = showCompareSheet
    )

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focus.clearFocus()
                    }
                )
            }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            Column(Modifier.fillMaxSize()) {
                LargeTopAppBar(
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier.drawHorizontalStroke(),
                    title = {
                        TopAppBarTitle(
                            title = viewModel.filterType?.let {
                                stringResource(it.title)
                            } ?: stringResource(R.string.filter),
                            input = viewModel.bitmap,
                            isLoading = viewModel.isImageLoading,
                            size = viewModel.bitmapSize ?: 0L
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                            3.dp
                        )
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = onBack
                        ) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, null)
                        }
                    },
                    actions = {
                        if (viewModel.previewBitmap != null) {
                            IconButton(
                                onClick = {
                                    showColorPicker.value = true
                                },
                                enabled = viewModel.previewBitmap != null
                            ) {
                                Icon(Icons.Outlined.Colorize, null)
                            }
                            IconButton(
                                onClick = {
                                    viewModel.performSharing { showConfetti() }
                                },
                                enabled = viewModel.canSave
                            ) {
                                Icon(Icons.Outlined.Share, null)
                            }
                        }
                        if (viewModel.bitmap == null) {
                            TopAppBarEmoji()
                        }
                        if (viewModel.bitmap != null && !imageInside) actions()
                        if (viewModel.bitmap != null && imageInside) {
                            when (viewModel.filterType) {
                                is Screen.Filter.Type.Basic -> {
                                    EnhancedIconButton(
                                        containerColor = MaterialTheme.colorScheme.mixedContainer,
                                        onClick = { showAddFilterSheet.value = true }
                                    ) {
                                        Icon(Icons.Rounded.PhotoFilter, null)
                                    }
                                }

                                is Screen.Filter.Type.Masking -> {
                                    EnhancedIconButton(
                                        containerColor = MaterialTheme.colorScheme.mixedContainer,
                                        onClick = { showAddMaskSheet.value = true }
                                    ) {
                                        Icon(Icons.Rounded.Texture, null)
                                    }
                                }

                                null -> Unit
                            }
                        }
                    }
                )

                val screenWidth = LocalConfiguration.current.screenWidthDp
                val easing = CubicBezierEasing(0.48f, 0.19f, 0.05f, 1.03f)
                AnimatedContent(
                    transitionSpec = {
                        if (targetState != null) {
                            slideInHorizontally(
                                animationSpec = tween(600, easing = easing),
                                initialOffsetX = { screenWidth }) + fadeIn(
                                tween(300, 100)
                            ) togetherWith slideOutHorizontally(
                                animationSpec = tween(600, easing = easing),
                                targetOffsetX = { -screenWidth }) + fadeOut(
                                tween(300, 100)
                            )
                        } else {
                            slideInHorizontally(
                                animationSpec = tween(600, easing = easing),
                                initialOffsetX = { -screenWidth }) + fadeIn(
                                tween(300, 100)
                            ) togetherWith slideOutHorizontally(
                                animationSpec = tween(600, easing = easing),
                                targetOffsetX = { screenWidth }) + fadeOut(
                                tween(300, 100)
                            )
                        } using SizeTransform(false)
                    },
                    targetState = viewModel.filterType
                ) { filterType ->
                    when (filterType) {
                        null -> {
                            var tempSelectionUris by rememberSaveable {
                                mutableStateOf<List<Uri>?>(
                                    null
                                )
                            }
                            val showSelectionFilterPicker =
                                rememberSaveable { mutableStateOf(false) }
                            LaunchedEffect(showSelectionFilterPicker.value) {
                                if (!showSelectionFilterPicker.value) tempSelectionUris = null
                            }
                            val selectionFilterPicker = rememberImagePicker(
                                mode = localImagePickerMode(Picker.Multiple)
                            ) { uris ->
                                uris.takeIf { it.isNotEmpty() }?.let {
                                    tempSelectionUris = it
                                    showSelectionFilterPicker.value = true
                                }
                            }

                            Column {
                                val cutout = WindowInsets.displayCutout.asPaddingValues()
                                LazyVerticalStaggeredGrid(
                                    modifier = Modifier.weight(1f),
                                    columns = StaggeredGridCells.Adaptive(300.dp),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        space = 12.dp,
                                        alignment = Alignment.CenterHorizontally
                                    ),
                                    verticalItemSpacing = 12.dp,
                                    contentPadding = PaddingValues(
                                        bottom = 12.dp + WindowInsets
                                            .navigationBars
                                            .asPaddingValues()
                                            .calculateBottomPadding(),
                                        top = 12.dp,
                                        end = 12.dp + cutout.calculateEndPadding(
                                            LocalLayoutDirection.current
                                        ),
                                        start = 12.dp + cutout.calculateStartPadding(
                                            LocalLayoutDirection.current
                                        )
                                    ),
                                ) {
                                    Screen.Filter.Type.entries.forEach {
                                        item {
                                            PreferenceItem(
                                                onClick = {
                                                    when (it) {
                                                        is Screen.Filter.Type.Basic -> pickImagesLauncher.pickImage()
                                                        is Screen.Filter.Type.Masking -> pickSingleImageLauncher.pickImage()
                                                    }
                                                },
                                                icon = it.icon,
                                                title = stringResource(it.title),
                                                subtitle = stringResource(it.subtitle),
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .drawHorizontalStroke(true)
                                        .background(
                                            MaterialTheme.colorScheme.surfaceColorAtElevation(
                                                3.dp
                                            )
                                        ),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    ExtendedFloatingActionButton(
                                        onClick = {
                                            selectionFilterPicker.pickImage()
                                        },
                                        modifier = Modifier
                                            .navigationBarsPadding()
                                            .padding(16.dp)
                                            .containerFabBorder(),
                                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                                        text = {
                                            Text(stringResource(R.string.pick_image_alt))
                                        },
                                        icon = {
                                            Icon(Icons.Rounded.AddPhotoAlternate, null)
                                        }
                                    )
                                }
                            }

                            SimpleSheet(
                                visible = showSelectionFilterPicker,
                                confirmButton = {
                                    EnhancedButton(
                                        onClick = {
                                            showSelectionFilterPicker.value = false
                                        },
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                                    ) {
                                        Text(stringResource(id = R.string.close))
                                    }
                                },
                                sheetContent = {
                                    if (tempSelectionUris == null) showSelectionFilterPicker.value =
                                        false

                                    if (tempSelectionUris?.size == 1) {
                                        LaunchedEffect(tempSelectionUris) {
                                            viewModel.setMaskFilter(tempSelectionUris?.firstOrNull())
                                            showSelectionFilterPicker.value = false
                                        }
                                    } else {
                                        LazyVerticalStaggeredGrid(
                                            columns = StaggeredGridCells.Adaptive(250.dp),
                                            horizontalArrangement = Arrangement.spacedBy(
                                                space = 12.dp,
                                                alignment = Alignment.CenterHorizontally
                                            ),
                                            verticalItemSpacing = 12.dp,
                                            contentPadding = PaddingValues(12.dp),
                                        ) {
                                            item {
                                                BasicFilterPreference(
                                                    onClick = {
                                                        viewModel.setBasicFilter(tempSelectionUris)
                                                        showSelectionFilterPicker.value = false
                                                    },
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                            item {
                                                MaskFilterPreference(
                                                    onClick = {
                                                        viewModel.setMaskFilter(tempSelectionUris?.firstOrNull())
                                                        showSelectionFilterPicker.value = false
                                                    },
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }
                                    }
                                },
                                title = {
                                    TitleItem(
                                        text = stringResource(id = R.string.pick_file),
                                        icon = Icons.Rounded.FileOpen
                                    )
                                }
                            )
                        }

                        else -> {
                            Box(Modifier.fillMaxSize()) {
                                if (filterType is Screen.Filter.Type.Basic) {
                                    val filterList = viewModel.basicFilterState.filters

                                    LaunchedEffect(filterList) {
                                        if (viewModel.needToApplyFilters) {
                                            viewModel.updatePreview()
                                        }
                                    }

                                    content(filterType)

                                    PickImageFromUrisSheet(
                                        transformations = listOf(
                                            ImageInfoTransformation(
                                                imageInfo = viewModel.imageInfo,
                                                filters = filterList,
                                                imageManager = viewModel.getImageManager()
                                            )
                                        ),
                                        visible = showPickImageFromUrisSheet,
                                        uris = viewModel.basicFilterState.uris,
                                        selectedUri = viewModel.basicFilterState.selectedUri,
                                        onUriPicked = { uri ->
                                            try {
                                                viewModel.setBitmap(uri = uri)
                                            } catch (e: Exception) {
                                                scope.launch {
                                                    toastHostState.showError(context, e)
                                                }
                                            }
                                        },
                                        onUriRemoved = { uri ->
                                            viewModel.updateUrisSilently(removedUri = uri)
                                        },
                                        columns = if (imageInside) 2 else 4,
                                    )

                                    AddFiltersSheet(
                                        visible = showAddFilterSheet,
                                        previewBitmap = viewModel.previewBitmap,
                                        onFilterPicked = { viewModel.addFilter(it.newInstance()) },
                                        onFilterPickedWithParams = { viewModel.addFilter(it) },
                                        imageManager = viewModel.getImageManager()
                                    )

                                    FilterReorderSheet(
                                        filterList = filterList,
                                        visible = showReorderSheet,
                                        updateOrder = viewModel::updateFiltersOrder
                                    )
                                } else if (filterType is Screen.Filter.Type.Masking) {
                                    LaunchedEffect(viewModel.maskingFilterState.masks) {
                                        if (viewModel.needToApplyFilters) {
                                            viewModel.updatePreview()
                                        }
                                    }

                                    content(filterType)

                                    if (imageInside || viewModel.bitmap == null) {
                                        Box(
                                            modifier = Modifier.align(settingsState.fabAlignment)
                                        ) {
                                            buttons(filterType)
                                        }
                                    }

                                    AddEditMaskSheet(
                                        visible = showAddMaskSheet,
                                        targetBitmapUri = viewModel.maskingFilterState.uri,
                                        onMaskPicked = viewModel::addMask,
                                        masks = viewModel.maskingFilterState.masks
                                    )

                                    MaskReorderSheet(
                                        maskList = viewModel.maskingFilterState.masks,
                                        visible = showReorderSheet,
                                        updateOrder = viewModel::updateMasksOrder
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (viewModel.isSaving) {
                LoadingDialog(
                    done = viewModel.done,
                    left = viewModel.left,
                    onCancelLoading = viewModel::cancelSaving
                )
            }

            ExitWithoutSavingDialog(
                onExit = {
                    if (viewModel.filterType != null) {
                        viewModel.clearType()
                    } else onGoBack()
                },
                onDismiss = { showExitDialog = false },
                visible = showExitDialog
            )

            BackHandler(onBack = onBack)
        }
    }
}