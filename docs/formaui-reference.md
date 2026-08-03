# FormaUI Component Reference

> Generated from `docs/component-inventory.json` in the forma-ui repo — regenerate there
> when the library version is bumped. Do not edit by hand.

**Artifacts** (Maven Central): `dev.formaui:core` and `dev.formaui:components`, version `0.2.0`.

```kotlin
dependencies {
    implementation("dev.formaui:components:0.2.0") // brings :core transitively
}
```

> Released through `0.1.0` under the retired `io.github.devsnackio` group; `dev.formaui` since `0.2.0`.
> Old coordinates still resolve via relocation POMs, but get no further releases.

Code packages are `dev.formaui.*`, matching the Maven group `dev.formaui` since `0.2.0`.
Import from `dev.formaui.*`.

**Setup rules:**
- Wrap every screen (or the app root) in `FormaTheme { ... }` (from `dev.formaui.core.theme`).
  Dynamic color is opt-in (`dynamicColor = true`); default palette is FormaUI's warm-editorial brand.
- Every public API requires `@OptIn(ExperimentalFormaUiApi::class)` (usually file-level).
- Always prefer `Forma*` components over raw Material 3 equivalents.
- Android only (`minSdk 24`) with Material 3. Built with Compose Multiplatform (JetBrains)
  `org.jetbrains.compose` artifacts; no other target is published.

**40 components.** Index:

| Component | Package | Summary |
|---|---|---|
| [Button](#button) | `button` | one entry point covering all five Material 3 button variants via a single variant parameter, with a default-on… |
| [TextField](#textfield) | `textfield` | one entry point covering both Material 3 text-field families via variant, with fully hoisted state. |
| [Card](#card) | `card` | a themed container with an opinionated header / content / footer slot API covering all three Material 3 card f… |
| [Chip](#chip) | `chip` | one entry point covering all four Material 3 chip types via variant, with a default-on press-scale micro-inter… |
| [Badge](#badge) | `badge` | a small status marker for indicating unread counts or presence. |
| [Switch](#switch) | `switch` | a themed on/off toggle for a single setting. |
| [Checkbox](#checkbox) | `checkbox` | a themed binary selection control. |
| [RadioButton](#radiobutton) | `radiobutton` | a themed control for choosing one option from a set. |
| [Dialog](#dialog) | `dialog` | a modal that interrupts the user for a decision or acknowledgement, delegating to Material 3's AlertDialog. |
| [BottomSheet](#bottomsheet) | `bottomsheet` | content that slides up from the bottom over a scrim, delegating to Material 3's ModalBottomSheet. |
| [NavigationBar](#navigationbar) | `navigation` | top-level destination switching, delegating to Material 3's NavigationBar. |
| [ListItem](#listitem) | `listitem` | a single row for lists, menus, and settings, delegating to Material 3's ListItem. |
| [Avatar](#avatar) | `avatar` | a circular (by default) tinted surface that centers its content. |
| [Divider](#divider) | `divider` | a thin rule for separating content, delegating to Material 3's HorizontalDivider / VerticalDivider. |
| [LoadingIndicator](#loadingindicator) | `loading` | delegating to Material 3's progress indicators. |
| [EmptyState](#emptystate) | `emptystate` | the centered "nothing here yet / no results" placeholder with an optional illustration, a title, a description… |
| [Snackbar](#snackbar) | `snackbar` | a brief, transient message at the bottom of the screen, delegating to Material 3's Snackbar, with a standard (… |
| [Slider](#slider) | `slider` | lets the user select a value from a continuous or stepped range, delegating to Material 3's Slider. |
| [BottomAppBar](#bottomappbar) | `bottomappbar` | navigation and key actions anchored to the bottom of small screens, delegating to Material 3's BottomAppBar. |
| [DropdownMenu](#dropdownmenu) | `menu` | a transient surface of choices anchored to a control, delegating to Material 3's DropdownMenu. |
| [FloatingActionButton](#floatingactionbutton) | `fab` | "Material 3 with better defaults"; one entry point covers all three Material 3 FAB sizes via size, with a defa… |
| [IconButton](#iconbutton) | `iconbutton` | "Material 3 with better defaults"; one entry point covers all four Material 3 icon button variants via variant… |
| [NavigationDrawer](#navigationdrawer) | `navigation` | an off-canvas panel of destinations that slides in over content and is dismissed with a scrim tap, delegating … |
| [NavigationRail](#navigationrail) | `navigation` | top-level destination switching for tablet/desktop-width screens, delegating to Material 3's NavigationRail. |
| [SearchBar](#searchbar) | `search` | "Material 3 with better defaults", wrapping Material 3's SearchBar/DockedSearchBar behind one entry point whos… |
| [SegmentedButton](#segmentedbutton) | `segmentedbutton` | a set of connected, mutually-styled buttons for view switching or option selection, delegating to Material 3's… |
| [TabRow](#tabrow) | `tabs` | switches between a small number of related content destinations, delegating to Material 3's PrimaryTabRow/Seco… |
| [Tooltip](#tooltip) | `tooltip` | "Material 3 with better defaults", wrapping Material 3's TooltipBox + PlainTooltip/RichTooltip. |
| [TopAppBar](#topappbar) | `topappbar` | "Material 3 with better defaults"; one entry point covers all four Material 3 top app bar sizes via variant. |
| [BarChart](#barchart) | `chart` | vertical bars with rounded top corners, drawn with pure Compose Canvas (no chart-library dependency); gridline… |
| [DonutChart](#donutchart) | `chart` | proportional stroked arc segments around an open center slot, drawn with pure Compose Canvas (no chart-library… |
| [LineChart](#linechart) | `chart` | a single series plotted as a smooth (or straight) trend line with an optional area fill, point markers, gridli… |
| [DatePickerSheet](#datepickersheet) | `datepicker` | Material 3's inline DatePicker hosted in a FormaBottomSheet instead of the M3 DatePickerDialog, with hoisted M… |
| [DateRangePickerSheet](#daterangepickersheet) | `datepicker` | Material 3's inline DateRangePicker hosted in a FormaBottomSheet instead of the M3 DatePickerDialog, with hois… |
| [PullToRefresh](#pulltorefresh) | `pulltorefresh` | wraps a scrollable layout and adds a swipe-down-to-refresh gesture, delegating to Material 3's stable PullToRe… |
| [SwipeToDismiss](#swipetodismiss) | `swipedismiss` | lets the user swipe a row horizontally to dismiss it (e.g. delete or archive), delegating to Material 3's stab… |
| [RangeSlider](#rangeslider) | `slider` | lets the user select a sub-range (start and end value) from a continuous or stepped range, delegating to Mater… |
| [TimePickerSheet](#timepickersheet) | `timepicker` | Material 3's TimePicker (clock dial) and TimeInput (text entry) hosted in a FormaBottomSheet, with hoisted M3 … |
| [Carousel](#carousel) | `carousel` | one entry point covering both Material 3 horizontal carousel families (multi-browse and uncontained) via a var… |
| [ExposedDropdownMenu](#exposeddropdownmenu) | `autocomplete` | a FormaTextField anchor that drops a menu of options below it, delegating to Material 3's ExposedDropdownMenuB… |

---

## Button

`dev.formaui.components.button` · tier: prd

A FormaUI button — "Material 3 with better defaults" — one entry point covering all five Material 3 button variants via a single variant parameter, with a default-on press-scale micro-interaction layered on the ripple.

**`FormaButton`** — One entry point for all five Material 3 button variants, with FormaUI shape/padding defaults and an enforced 48dp minimum touch target.

```kotlin
fun FormaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: FormaButtonVariant = FormaButtonVariant.Filled,
    enabled: Boolean = true,
    shape: Shape = FormaButtonDefaults.shape,
    colors: ButtonColors? = null,
    contentPadding: PaddingValues = FormaButtonDefaults.contentPadding,
    interactionSource: MutableInteractionSource? = null,
    pressedScale: Float = FormaPressScaleDefaults.PressedScale,
    pressAnimationSpec: FiniteAnimationSpec<Float>? = FormaPressScaleDefaults.AnimationSpec,
    content: @Composable RowScope.() -> Unit,
)
```

- `onClick` — Called when the button is clicked. Not invoked while enabled is false.
- `modifier` — The Modifier applied to the button. A minimum height of FormaButtonDefaults.MinTouchTargetSize is always merged in.
- `variant` — The visual emphasis (defaults to FormaButtonVariant.Filled).
- `enabled` — Whether the button is interactive. When false it is visually de-emphasised and does not respond to input.
- `shape` — The button's container shape (defaults to FormaButtonDefaults.shape).
- `colors` — The container/content colors. When null, the Material 3 default colors for the chosen variant are used.
- `contentPadding` — Padding between the container edges and the content (defaults to FormaButtonDefaults.contentPadding).
- `interactionSource` — The MutableInteractionSource for observing/emitting interactions. When null, one is remembered internally.
- `pressedScale` — The scale factor applied while the button is pressed (defaults to a subtle 0.97). Must be greater than 0.
- `pressAnimationSpec` — The animation used for the press-scale's release spring-back (defaults to a responsive spring; the press dip uses FormaPressScaleDefaults.DownAnimationSpec). Pass null to disable the press-scale entirely.
- `content` — The button's content, laid out in a RowScope (typically a Text, optionally with a leading/trailing Icon).

**Supporting API:**
- `FormaButtonVariant` (enum) — Visual emphasis of a FormaButton, listed highest to lowest: Filled, Tonal, Elevated, Outlined, Text.
- `FormaButtonDefaults` (object) — Defaults for FormaButton: MinTouchTargetSize (48dp), shape (FormaTheme.shapes.md), contentPadding (lg horizontal / xs vertical spacing tokens).
- `Modifier.formaPressScale` (modifier) — Reusable press-scale micro-interaction (dev.formaui.components.interaction): observes an InteractionSource and scales the element to pressedScale while pressed via graphicsLayer, so measured size and the 48dp touch target are unaffected. animationSpec = null disables it.
- `FormaPressScaleDefaults` (object) — Defaults for Modifier.formaPressScale: PressedScale (0.97), DownAnimationSpec (a fast 100ms tween for the press dip), and AnimationSpec (a medium-stiffness, low-bouncy spring for the release spring-back).

**Variants:** filled, tonal, elevated, outlined, text

**Example:**

```kotlin
FormaButton(onClick = { /* submit */ }) {
    Text("Confirm")
}

FormaButton(onClick = { }, variant = FormaButtonVariant.Outlined, enabled = false) {
    Text("Disabled")
}
```

**Accessibility:** A minimum 48dp touch target (FormaButtonDefaults.MinTouchTargetSize) is always enforced; Role.Button semantics and disabled state are inherited from the underlying Material 3 button.

---

## TextField

`dev.formaui.components.textfield` · tier: prd

A FormaUI text field — "Material 3 with better defaults" — one entry point covering both Material 3 text-field families via variant, with fully hoisted state.

**`FormaTextField`** — One entry point for both Material 3 text-field families (outlined and filled), with hoisted value state and helper/error supporting text on one line below the field.

```kotlin
fun FormaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    variant: FormaTextFieldVariant = FormaTextFieldVariant.Outlined,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    helperText: String? = null,
    errorText: String? = null,
    singleLine: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape? = null,
    colors: TextFieldColors? = null,
    interactionSource: MutableInteractionSource? = null,
)
```

- `value` — The current text to display.
- `onValueChange` — Called with the updated text as the user edits. The caller must hoist and persist the new value for it to appear.
- `modifier` — The Modifier applied to the field.
- `variant` — The container style (defaults to FormaTextFieldVariant.Outlined).
- `enabled` — Whether the field is editable and focusable. When false it is visually de-emphasised and ignores input.
- `readOnly` — Whether the field is read-only: it shows value and is focusable/selectable but not editable.
- `label` — Optional floating label describing the field.
- `placeholder` — Optional placeholder shown while the field is empty.
- `leadingIcon` — Optional slot rendered at the start of the field.
- `trailingIcon` — Optional slot rendered at the end of the field.
- `isError` — Whether the field is in the error state (error styling + accessibility signal).
- `helperText` — Optional supporting text shown below the field when not in error.
- `errorText` — Optional supporting text shown below the field while isError is true; takes precedence over helperText.
- `singleLine` — Whether the field is constrained to a single line.
- `textStyle` — The TextStyle applied to the input text (defaults to the ambient text style; pass FormaTheme.typography.numeric for tabular numeric entry).
- `visualTransformation` — Transforms the visual representation of value (e.g. password dots).
- `keyboardOptions` — Software-keyboard configuration (type, IME action, capitalization).
- `keyboardActions` — Callbacks for IME actions.
- `shape` — The container shape. When null, the variant's sensible default is used (FormaTextFieldDefaults.outlinedShape for outlined, M3's shape for filled).
- `colors` — The field colors. When null, the M3 default colors for the variant are used.
- `interactionSource` — The MutableInteractionSource for observing/emitting interactions (focus, press). When null, one is remembered internally.

**Supporting API:**
- `FormaTextFieldVariant` (enum) — Container style of a FormaTextField: Outlined (default), Filled.
- `FormaTextFieldDefaults` (object) — Defaults for FormaTextField: outlinedShape (FormaTheme.shapes.md).

**Variants:** outlined, filled

**Example:**

```kotlin
var amount by remember { mutableStateOf("") }
FormaTextField(
    value = amount,
    onValueChange = { amount = it },
    label = "Amount",
    helperText = "Available balance: $5,000.00",
    singleLine = true,
)
```

**Accessibility:** isError applies error styling and surfaces the error state to accessibility services via the underlying M3 field; supply a contentDescription on any leading/trailing icon that conveys meaning.

---

## Card

`dev.formaui.components.card` · tier: prd

A FormaUI card — a themed container with an opinionated header / content / footer slot API covering all three Material 3 card families via variant; clickable cards get a default-on press-scale micro-interaction (shared Modifier.formaPressScale).

**`FormaCard`** — A themed container with header / content / footer slots stacked vertically, optionally clickable via onClick, covering all three M3 card families.

```kotlin
fun FormaCard(
    modifier: Modifier = Modifier,
    variant: FormaCardVariant = FormaCardVariant.Elevated,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    shape: Shape = FormaCardDefaults.shape,
    colors: CardColors? = null,
    contentPadding: PaddingValues = FormaCardDefaults.contentPadding,
    interactionSource: MutableInteractionSource? = null,
    pressedScale: Float = FormaCardDefaults.PressedScale,
    pressAnimationSpec: FiniteAnimationSpec<Float>? = FormaPressScaleDefaults.AnimationSpec,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
)
```

- `modifier` — The Modifier applied to the card.
- `variant` — The surface style (defaults to FormaCardVariant.Elevated).
- `onClick` — Optional click handler. When non-null the entire card becomes clickable and focusable; when null the card is a static container.
- `enabled` — Whether the card responds to clicks. Only meaningful when onClick is non-null.
- `shape` — The card's container shape (defaults to FormaCardDefaults.shape).
- `colors` — The card colors. When null, the M3 default colors for the variant are used.
- `contentPadding` — Padding between the card edges and its slots (defaults to FormaCardDefaults.contentPadding).
- `interactionSource` — The MutableInteractionSource for observing/emitting interactions. When null, one is remembered internally for the clickable card. Unused when onClick is null.
- `pressedScale` — The scale factor applied while a clickable card is pressed (defaults to a subtler 0.98 — large surfaces read a small dip clearly). Must be greater than 0. Unused when onClick is null.
- `pressAnimationSpec` — The animation used for the press-scale's release spring-back (defaults to a responsive spring; the press dip uses FormaPressScaleDefaults.DownAnimationSpec). Pass null to disable the press-scale entirely. Unused when onClick is null.
- `header` — Optional slot rendered above content (e.g. a title row).
- `footer` — Optional slot rendered below content (e.g. an action row).
- `content` — The card's main content, laid out in a ColumnScope.

**Supporting API:**
- `FormaCardVariant` (enum) — Surface style of a FormaCard: Elevated (default), Outlined, Filled.
- `FormaCardDefaults` (object) — Defaults for FormaCard: shape (FormaTheme.shapes.lg), contentPadding (FormaTheme.spacing.md on all sides), PressedScale (0.98 — a subtler press-scale dip for large surfaces).

**Variants:** elevated, outlined, filled

**Example:**

```kotlin
FormaCard(
    header = { Text("Account", style = MaterialTheme.typography.titleMedium) },
    footer = {
        FormaButton(onClick = { }, variant = FormaButtonVariant.Text) {
            Text("View details")
        }
    },
) {
    Text("Balance")
    Text("$1,240.00", style = FormaTheme.typography.numeric)
}
```

**Accessibility:** When onClick is non-null the entire card is a single clickable/focusable target; Role.Button semantics and the ripple are handled by the underlying M3 clickable card.

---

## Chip

`dev.formaui.components.chip` · tier: prd

A FormaUI chip — "Material 3 with better defaults" — one entry point covering all four Material 3 chip types via variant, with a default-on press-scale micro-interaction (shared Modifier.formaPressScale).

**`FormaChip`** — One entry point for all four Material 3 chip types; selected is honored only by the Filter and Input variants, and trailingIcon is ignored for Suggestion.

```kotlin
fun FormaChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: FormaChipVariant = FormaChipVariant.Assist,
    selected: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape? = null,
    interactionSource: MutableInteractionSource? = null,
    pressedScale: Float = FormaPressScaleDefaults.PressedScale,
    pressAnimationSpec: FiniteAnimationSpec<Float>? = FormaPressScaleDefaults.AnimationSpec,
)
```

- `label` — The chip's text label.
- `onClick` — Called when the chip is clicked. For selectable variants, toggle selected in response.
- `modifier` — The Modifier applied to the chip.
- `variant` — The chip kind (defaults to FormaChipVariant.Assist).
- `selected` — Whether the chip is selected. Only meaningful for FormaChipVariant.Filter and FormaChipVariant.Input.
- `enabled` — Whether the chip is interactive.
- `leadingIcon` — Optional slot at the start of the chip (for FormaChipVariant.Suggestion this is the chip's single icon).
- `trailingIcon` — Optional slot at the end of the chip. Ignored for FormaChipVariant.Suggestion.
- `shape` — The chip's container shape (defaults to FormaChipDefaults.shape).
- `interactionSource` — The MutableInteractionSource for observing/emitting interactions. When null, one is remembered internally.
- `pressedScale` — The scale factor applied while the chip is pressed (defaults to a subtle 0.97). Must be greater than 0.
- `pressAnimationSpec` — The animation used for the press-scale's release spring-back (defaults to a responsive spring; the press dip uses FormaPressScaleDefaults.DownAnimationSpec). Pass null to disable the press-scale entirely.

**Supporting API:**
- `FormaChipVariant` (enum) — Kind of a FormaChip: Assist, Filter, Input, Suggestion.
- `FormaChipDefaults` (object) — Defaults for FormaChip: shape (FormaTheme.shapes.md).

**Variants:** assist, filter, input, suggestion

**Example:**

```kotlin
var selected by remember { mutableStateOf(false) }
FormaChip(
    label = "Filter",
    onClick = { selected = !selected },
    variant = FormaChipVariant.Filter,
    selected = selected,
)
FormaChip(label = "Assist", onClick = { })
```

**Accessibility:** Chips keep Material 3's compact visual height while automatically exposing a 48dp touch target via M3's minimum interactive component size — no explicit height is forced.

---

## Badge

`dev.formaui.components.badge` · tier: prd

A FormaUI badge — a small status marker for indicating unread counts or presence.

**`FormaBadge`** — A small status marker: a dot badge when count is null, otherwise a numeric badge capped at maxCount with a trailing + (e.g. 99+).

```kotlin
fun FormaBadge(
    modifier: Modifier = Modifier,
    count: Int? = null,
    maxCount: Int = DefaultMaxCount,
    containerColor: Color = MaterialTheme.colorScheme.error,
    contentColor: Color = contentColorFor(containerColor),
)
```

- `modifier` — The Modifier applied to the badge.
- `count` — The number to display, or null for a dot badge.
- `maxCount` — The largest number shown verbatim; above it the badge shows "$maxCount+" (defaults to 99). Ignored for dot badges.
- `containerColor` — The badge background color (defaults to the M3 error color).
- `contentColor` — The badge text color (defaults to the content color for containerColor).

**`FormaBadgedBox`** — Anchors a badge to the top-end corner of some content — the FormaUI wrapper over Material 3's BadgedBox.

```kotlin
fun FormaBadgedBox(
    badge: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
)
```

- `badge` — The badge to draw at the content's top-end corner (typically a FormaBadge).
- `modifier` — The Modifier applied to the box.
- `content` — The element the badge is anchored to.

**Variants:** dot, numeric

**Example:**

```kotlin
FormaBadgedBox(badge = { FormaBadge(count = 5) }) {
    Text("Inbox")
}
FormaBadgedBox(badge = { FormaBadge() }) {
    Text("Updates")
}
```

**Accessibility:** The badge is decorative; put the meaningful description (including any count) on the annotated element's contentDescription (e.g. "Messages, 5 unread") — not on the badge — so screen readers announce it in context.

---

## Switch

`dev.formaui.components.switch` · tier: prd

A FormaUI switch — a themed on/off toggle for a single setting.

**`FormaSwitch`** — A stateless on/off toggle with hoisted checked state; pass onCheckedChange = null for a display-only switch toggled elsewhere.

```kotlin
fun FormaSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    thumbContent: (@Composable () -> Unit)? = null,
    colors: SwitchColors? = null,
    interactionSource: MutableInteractionSource? = null,
)
```

- `checked` — Whether the switch is on.
- `onCheckedChange` — Called with the new value when the user toggles the switch, or null to make the switch non-interactive.
- `modifier` — The Modifier applied to the switch.
- `enabled` — Whether the switch is interactive.
- `thumbContent` — Optional slot drawn inside the thumb (e.g. a check icon when checked).
- `colors` — The switch colors (defaults to the M3 defaults, themed by FormaTheme).
- `interactionSource` — The MutableInteractionSource for observing/emitting interactions. When null, one is remembered internally.

**Example:**

```kotlin
var on by remember { mutableStateOf(false) }
FormaSwitch(
    checked = on,
    onCheckedChange = { on = it },
)
```

**Accessibility:** The interactive control exposes a 48dp touch target via Material 3's minimum interactive component size; pass onCheckedChange = null to render a display-only switch whose toggling is handled elsewhere (e.g. by a click on an enclosing row).

---

## Checkbox

`dev.formaui.components.checkbox` · tier: prd

A FormaUI checkbox — a themed binary selection control.

**`FormaCheckbox`** — A stateless binary selection control with hoisted checked state; pass onCheckedChange = null when an enclosing toggleable row owns the toggle.

```kotlin
fun FormaCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors? = null,
    interactionSource: MutableInteractionSource? = null,
)
```

- `checked` — Whether the box is checked.
- `onCheckedChange` — Called with the new value when toggled, or null to make the checkbox non-interactive.
- `modifier` — The Modifier applied to the checkbox.
- `enabled` — Whether the checkbox is interactive.
- `colors` — The checkbox colors (defaults to the M3 defaults, themed by FormaTheme).
- `interactionSource` — The MutableInteractionSource for observing/emitting interactions. When null, one is remembered internally.

**Example:**

```kotlin
var checked by remember { mutableStateOf(true) }
Row(Modifier.toggleable(
    value = checked,
    role = Role.Checkbox,
    onValueChange = { checked = it },
)) {
    FormaCheckbox(checked = checked, onCheckedChange = null)
    Text("Enable notifications")
}
```

**Accessibility:** 48dp touch target via Material 3's minimum interactive component size; when placing next to a label, prefer toggling from the row (Modifier.toggleable with Role.Checkbox) and pass onCheckedChange = null so the whole row is one accessible target.

---

## RadioButton

`dev.formaui.components.radiobutton` · tier: prd

A FormaUI radio button — a themed control for choosing one option from a set.

**`FormaRadioButton`** — A stateless single-choice control with hoisted selected state; group several with Modifier.selectableGroup() and pass onClick = null when the row owns selection.

```kotlin
fun FormaRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors? = null,
    interactionSource: MutableInteractionSource? = null,
)
```

- `selected` — Whether this option is the selected one in its group.
- `onClick` — Called when this option is clicked, or null to make the radio button non-interactive.
- `modifier` — The Modifier applied to the radio button.
- `enabled` — Whether the radio button is interactive.
- `colors` — The radio button colors (defaults to the M3 defaults, themed by FormaTheme).
- `interactionSource` — The MutableInteractionSource for observing/emitting interactions. When null, one is remembered internally.

**Example:**

```kotlin
val options = listOf("Standard", "Priority", "Overnight")
var selected by remember { mutableStateOf(options.first()) }
Column(Modifier.selectableGroup()) {
    options.forEach { option ->
        Row(Modifier.selectable(
            selected = option == selected,
            role = Role.RadioButton,
            onClick = { selected = option },
        )) {
            FormaRadioButton(selected = option == selected, onClick = null)
            Text(option)
        }
    }
}
```

**Accessibility:** 48dp touch target via Material 3's minimum interactive component size; apply Modifier.selectableGroup() to the container so screen readers announce the set and position (e.g. "2 of 4"), and prefer row-level Modifier.selectable with onClick = null so the whole row is one accessible target.

---

## Dialog

`dev.formaui.components.dialog` · tier: prd

A FormaUI alert dialog — a modal that interrupts the user for a decision or acknowledgement, delegating to Material 3's AlertDialog.

**`FormaAlertDialog`** — Modal alert dialog for a decision or acknowledgement; shown while composed, with hoisted visibility state.

```kotlin
fun FormaAlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    title: String? = null,
    text: String? = null,
    shape: Shape = FormaDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    titleTextStyle: TextStyle? = null,
    textTextStyle: TextStyle? = null,
)
```

- `onDismissRequest` — called when the dialog is dismissed by tapping outside or pressing back.
- `confirmButton` — the primary action (required).
- `modifier` — the Modifier applied to the dialog surface.
- `dismissButton` — the optional secondary/cancel action.
- `icon` — optional icon shown above the title.
- `title` — optional dialog title.
- `text` — optional supporting body text.
- `shape` — the dialog container shape (defaults to FormaDialogDefaults.shape).
- `containerColor` — the dialog's background color (defaults to the M3 default, themed by FormaTheme).
- `iconContentColor` — the color applied to icon (defaults to the M3 default).
- `titleContentColor` — the color applied to title (defaults to the M3 default).
- `textContentColor` — the color applied to text (defaults to the M3 default).
- `titleTextStyle` — optional TextStyle override for title, merged on top of the M3 title style.
- `textTextStyle` — optional TextStyle override for text, merged on top of the M3 text style.

**`FormaFullScreenDialog`** — Full-screen modal dialog for a focused multi-step task, with a top bar hosting Close, the title, and an optional confirm action.

```kotlin
fun FormaFullScreenDialog(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    titleTextStyle: TextStyle? = null,
    confirmAction: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
)
```

- `onDismissRequest` — called when the user closes the dialog (Close button or back press).
- `title` — the dialog's title, shown in the top bar.
- `modifier` — the Modifier applied to the dialog surface.
- `containerColor` — the dialog surface's background color (defaults to the M3 surface, themed by FormaTheme).
- `contentColor` — the preferred content color inside the dialog (defaults to the color matching containerColor).
- `titleTextStyle` — optional TextStyle override for title, merged on top of the top bar's title style (M3 titleLarge).
- `confirmAction` — optional top-bar action slot at the end (e.g. a "Save" FormaButton).
- `content` — the dialog body, laid out in a scrollable ColumnScope.

**Supporting API:**
- `FormaDialogDefaults` (object) — Default values for FormaUI dialogs; shape returns FormaTheme.shapes.xl.

**Variants:** Alert, Full-screen

**Example:**

```kotlin
var showDialog by remember { mutableStateOf(true) }
if (showDialog) {
    FormaAlertDialog(
        onDismissRequest = { showDialog = false },
        title = "Delete payment?",
        text = "This action can't be undone.",
        confirmButton = {
            FormaButton(onClick = {}) { Text("Delete") }
        },
        dismissButton = {
            FormaButton(onClick = { showDialog = false }, variant = FormaButtonVariant.Text) { Text("Cancel") }
        },
    )
}
```

**Accessibility:** Both variants delegate dismissal to the underlying Dialog, so an outside tap or back press routes to onDismissRequest; FormaFullScreenDialog's Close control is a text FormaButton, so it announces its label without needing a contentDescription. Pass contentDescription = null on FormaAlertDialog's icon when the title already conveys the same meaning.

---

## BottomSheet

`dev.formaui.components.bottomsheet` · tier: prd

A FormaUI modal bottom sheet — content that slides up from the bottom over a scrim, delegating to Material 3's ModalBottomSheet.

**`FormaBottomSheet`** — Modal bottom sheet shown while composed; the Material 3 SheetState is managed internally so callers only need FormaUI's opt-in.

```kotlin
fun FormaBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    skipPartiallyExpanded: Boolean = false,
    shape: Shape? = null,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    content: @Composable ColumnScope.() -> Unit,
)
```

- `onDismissRequest` — called when the sheet is dismissed.
- `modifier` — the Modifier applied to the sheet.
- `skipPartiallyExpanded` — when true, the sheet has no half-expanded state and opens fully.
- `shape` — the sheet container shape. When null, Material 3's top-rounded default is used.
- `containerColor` — the sheet's background color (defaults to the M3 bottom-sheet container, themed by FormaTheme).
- `contentColor` — the preferred content color inside the sheet (defaults to the color matching containerColor).
- `scrimColor` — the color of the scrim that obscures content behind the sheet (defaults to the M3 default scrim).
- `content` — the sheet's content, laid out in a ColumnScope.

**Variants:** Modal, Modal (skipPartiallyExpanded)

**Example:**

```kotlin
var open by remember { mutableStateOf(false) }
FormaButton(onClick = { open = true }) { Text("Show sheet") }
if (open) {
    FormaBottomSheet(onDismissRequest = { open = false }) {
        Text("Payment options", style = MaterialTheme.typography.titleMedium)
        Text("Choose how you'd like to pay.")
        FormaButton(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Continue") }
    }
}
```

**Accessibility:** Delegates to Material 3's ModalBottomSheet, which holds focus above the scrim and routes a scrim tap, back press, or swipe-down to onDismissRequest; the default M3 drag handle (and its semantics) comes with the sheet. The sheet itself carries no title semantics — give its content a heading Text of its own.

---

## NavigationBar

`dev.formaui.components.navigation` · tier: prd

A FormaUI bottom navigation bar — top-level destination switching, delegating to Material 3's NavigationBar.

**`FormaNavigationBar`** — Bottom navigation bar container; populate its RowScope content with FormaNavigationBarItems.

```kotlin
fun FormaNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable RowScope.() -> Unit,
)
```

- `modifier` — the Modifier applied to the bar.
- `containerColor` — the bar's background color (defaults to the M3 navigation-bar container, themed by FormaTheme).
- `contentColor` — the preferred content color for items (defaults to the color matching containerColor).
- `content` — the bar's items, laid out in a RowScope (typically FormaNavigationBarItems).

**`FormaNavigationBarItem`** — A single destination in a FormaNavigationBar (RowScope extension) with built-in numeric or dot badge support on the icon.

```kotlin
fun FormaNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    badgeCount: Int? = null,
    showBadgeDot: Boolean = false,
    alwaysShowLabel: Boolean = true,
    colors: NavigationBarItemColors? = null,
    labelTextStyle: TextStyle? = null,
)
```

- `selected` — whether this item is the current destination.
- `onClick` — called when the item is tapped.
- `icon` — the item's icon (supply a contentDescription on it for accessibility).
- `modifier` — the Modifier applied to the item.
- `label` — optional text label shown under the icon.
- `enabled` — whether the item is interactive.
- `badgeCount` — optional unread count to show as a numeric badge on the icon.
- `showBadgeDot` — when true and badgeCount is null, shows a dot badge on the icon.
- `alwaysShowLabel` — whether the label is shown even when the item is unselected.
- `colors` — the item colors — icon, label, and selection-indicator colors for the selected, unselected, and disabled states. Defaults to the M3 defaults, themed by FormaTheme.
- `labelTextStyle` — optional TextStyle override for label, merged on top of the M3 label style so a partial override (e.g. only fontWeight) keeps the M3 defaults for everything else.

**Variants:** No badge, Numeric badge, Dot badge

**Example:**

```kotlin
var selected by remember { mutableIntStateOf(0) }
FormaNavigationBar {
    FormaNavigationBarItem(
        selected = selected == 0, onClick = { selected = 0 },
        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
        label = "Home",
    )
    FormaNavigationBarItem(
        selected = selected == 1, onClick = { selected = 1 },
        icon = { Icon(Icons.Default.Notifications, contentDescription = "Alerts") },
        label = "Alerts", badgeCount = 3,
    )
}
```

**Accessibility:** 48dp touch target and selection semantics come from Material 3's NavigationBarItem; supply a contentDescription on each item's icon for accessibility.

---

## ListItem

`dev.formaui.components.listitem` · tier: prd

A FormaUI list item — a single row for lists, menus, and settings, delegating to Material 3's ListItem.

**`FormaListItem`** — List row whose single-/two-/three-line layout is derived automatically from which of headline/overline/supporting are supplied, with optional leading/trailing slots and click handling.

```kotlin
fun FormaListItem(
    headline: String,
    modifier: Modifier = Modifier,
    overline: String? = null,
    supporting: String? = null,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    colors: ListItemColors? = null,
    headlineTextStyle: TextStyle? = null,
    overlineTextStyle: TextStyle? = null,
    supportingTextStyle: TextStyle? = null,
)
```

- `headline` — the primary text of the row.
- `modifier` — the Modifier applied to the row.
- `overline` — optional label shown above headline.
- `supporting` — optional secondary text shown below headline.
- `leading` — optional slot at the start of the row.
- `trailing` — optional slot at the end of the row.
- `onClick` — optional click handler; when non-null the whole row becomes clickable.
- `enabled` — whether the row responds to clicks. Only meaningful when onClick is non-null.
- `colors` — the row colors (defaults to the M3 defaults, themed by FormaTheme).
- `headlineTextStyle` — optional TextStyle override for headline, merged on top of the M3 headline style so a partial override (e.g. only fontWeight) keeps the M3 defaults for everything else.
- `overlineTextStyle` — optional TextStyle override for overline, merged on top of the M3 overline style.
- `supportingTextStyle` — optional TextStyle override for supporting, merged on top of the M3 supporting style.

**Variants:** Single-line, Two-line, Three-line, Clickable

**Example:**

```kotlin
Column {
    FormaListItem(
        headline = "Single line",
        trailing = { Text("›") },
        onClick = {},
    )
    FormaDivider()
    FormaListItem(
        headline = "Ada Lovelace",
        supporting = "ada@example.com",
        leading = { FormaAvatar(initials = "AL", size = FormaAvatarSize.Small) },
        onClick = {},
    )
}
```

**Accessibility:** Passing onClick makes the whole row a single clickable/focusable target with Role.Button semantics; M3 list items already meet the 48dp minimum height.

---

## Avatar

`dev.formaui.components.avatar` · tier: prd

A FormaUI avatar container — a circular (by default) tinted surface that centers its content.

**`FormaAvatar`** — Base avatar container (slot overload): fill content with an Image, Icon, or text, centered on a circular tinted surface.

```kotlin
fun FormaAvatar(
    modifier: Modifier = Modifier,
    size: FormaAvatarSize = FormaAvatarSize.Medium,
    shape: Shape = FormaAvatarDefaults.shape,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    content: @Composable BoxScope.() -> Unit,
)
```

- `modifier` — the Modifier applied to the avatar.
- `size` — the avatar diameter (defaults to FormaAvatarSize.Medium).
- `shape` — the clip shape (defaults to FormaAvatarDefaults.shape, a full circle).
- `containerColor` — the background color (defaults to the M3 primaryContainer).
- `contentColor` — the color applied to icon/text content (defaults to M3 onPrimaryContainer).
- `content` — the centered content (an Image, Icon, or text).

**`FormaAvatar`** — Initials overload — the standard fallback when a user has no profile image.

```kotlin
fun FormaAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: FormaAvatarSize = FormaAvatarSize.Medium,
    shape: Shape = FormaAvatarDefaults.shape,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    textStyle: TextStyle = FormaAvatarDefaults.textStyle(size),
)
```

- `initials` — the initials to display (typically 1–2 characters).
- `modifier` — the Modifier applied to the avatar.
- `size` — the avatar diameter (defaults to FormaAvatarSize.Medium).
- `shape` — the clip shape (defaults to FormaAvatarDefaults.shape, a full circle).
- `containerColor` — the background color (defaults to the M3 primaryContainer).
- `contentColor` — the initials color (defaults to M3 onPrimaryContainer).
- `textStyle` — the initials TextStyle (defaults to FormaAvatarDefaults.textStyle for size, which keeps the type at roughly 35–40% of the avatar diameter).

**Supporting API:**
- `FormaAvatarSize` (enum) — Avatar diameter: Small (32dp), Medium (40dp), Large (56dp); exposes a dp property.
- `FormaAvatarDefaults` (object) — Default values for FormaAvatar; shape returns FormaTheme.shapes.full (circular); textStyle(size) maps the avatar size to the theme type scale (Small → labelMedium, Medium → titleSmall, Large → titleLarge, roughly 35–40% of the diameter).

**Variants:** Initials, Icon, Image, Small, Medium, Large

**Example:**

```kotlin
Row(horizontalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md)) {
    FormaAvatar(initials = "AB", size = FormaAvatarSize.Small)
    FormaAvatar(initials = "CD")
    FormaAvatar(initials = "EF", size = FormaAvatarSize.Large)
    // Slot content: any composable can fill the avatar container.
    FormaAvatar { Text("★") }
}
```

**Accessibility:** The container is decorative and non-interactive; wrap it in a clickable/Modifier.semantics parent (with the person's name as the description) when it represents a tappable user.

---

## Divider

`dev.formaui.components.divider` · tier: prd

A FormaUI divider — a thin rule for separating content, delegating to Material 3's HorizontalDivider / VerticalDivider.

**`FormaDivider`** — Thin horizontal or vertical rule for separating content, chosen by the orientation parameter.

```kotlin
fun FormaDivider(
    modifier: Modifier = Modifier,
    orientation: FormaDividerOrientation = FormaDividerOrientation.Horizontal,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color,
)
```

- `modifier` — the Modifier applied to the divider.
- `orientation` — whether the rule runs horizontally or vertically (defaults to FormaDividerOrientation.Horizontal).
- `thickness` — the stroke thickness (defaults to Material 3's hairline DividerDefaults.Thickness).
- `color` — the stroke color (defaults to Material 3's DividerDefaults.color).

**Supporting API:**
- `FormaDividerOrientation` (enum) — The direction the divider runs: Horizontal, Vertical.

**Variants:** Horizontal, Vertical

**Example:**

```kotlin
Column(verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md)) {
    Text("Above")
    FormaDivider()
    Text("Below")
    Row(modifier = Modifier.height(FormaTheme.spacing.xl)) {
        Text("Left")
        FormaDivider(orientation = FormaDividerOrientation.Vertical)
        Text("Right")
    }
}
```

**Accessibility:** Purely decorative: a divider carries no semantics and does not need a content description.

---

## LoadingIndicator

`dev.formaui.components.loading` · tier: prd

A FormaUI loading indicator — circular or linear, determinate or indeterminate — delegating to Material 3's progress indicators.

**`FormaLoadingIndicator`** — Circular or linear progress indicator; indeterminate when progress is null, determinate for a 0f..1f fraction.

```kotlin
fun FormaLoadingIndicator(
    modifier: Modifier = Modifier,
    variant: FormaLoadingIndicatorVariant = FormaLoadingIndicatorVariant.Circular,
    progress: Float? = null,
    contentDescription: String? = null,
)
```

- `modifier` — the Modifier applied to the indicator.
- `variant` — circular or linear (defaults to FormaLoadingIndicatorVariant.Circular).
- `progress` — the completion fraction 0f..1f, or null for an indeterminate indicator.
- `contentDescription` — optional accessibility description of what is loading.

**Supporting API:**
- `FormaLoadingIndicatorVariant` (enum) — The indicator's shape: Circular, Linear.

**Variants:** Circular indeterminate, Circular determinate, Linear indeterminate, Linear determinate

**Example:**

```kotlin
Column(verticalArrangement = Arrangement.spacedBy(FormaTheme.spacing.md)) {
    FormaLoadingIndicator(contentDescription = "Loading")
    FormaLoadingIndicator(progress = 0.6f, contentDescription = "60 percent loaded")
    FormaLoadingIndicator(
        modifier = Modifier.fillMaxWidth(),
        variant = FormaLoadingIndicatorVariant.Linear,
        contentDescription = "Loading",
    )
}
```

**Accessibility:** Supply contentDescription (e.g. "Loading transactions") so screen readers announce the busy state; a bare indicator is otherwise silent.

---

## EmptyState

`dev.formaui.components.emptystate` · tier: prd

A FormaUI empty state — the centered "nothing here yet / no results" placeholder with an optional illustration, a title, a description, and an optional action.

**`FormaEmptyState`** — Centered empty-state placeholder composed from FormaUI tokens (no Material 3 equivalent); only title is required.

```kotlin
fun FormaEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: @Composable (() -> Unit)? = null,
    action: @Composable (() -> Unit)? = null,
)
```

- `title` — the primary message (required).
- `modifier` — the Modifier applied to the empty state.
- `description` — optional secondary explanatory text.
- `icon` — optional illustration/icon slot shown above the title.
- `action` — optional action slot shown below the text (typically a FormaButton).

**Variants:** Title only, With description, With icon, With action

**Example:**

```kotlin
FormaEmptyState(
    title = "No transactions yet",
    description = "Your transactions will appear here once you make your first payment.",
    icon = { Icon(Icons.Outlined.Receipt, contentDescription = null) },
    action = { FormaButton(onClick = { /* add payment */ }) { Text("Add payment") } },
)
```

**Accessibility:** title and description render as real Text, so both are announced in order; the icon slot is decorative — pass contentDescription = null on it — and any action should be a labelled control such as a FormaButton.

---

## Snackbar

`dev.formaui.components.snackbar` · tier: prd

A FormaUI snackbar — a brief, transient message at the bottom of the screen, delegating to Material 3's Snackbar, with a standard (message-only) and an action variant.

**`FormaSnackbar`** — Renders the snackbar visual directly; the action button is shown only when both actionLabel and onAction are provided.

```kotlin
fun FormaSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    shape: Shape = FormaSnackbarDefaults.shape,
    containerColor: Color = SnackbarDefaults.color,
    contentColor: Color = SnackbarDefaults.contentColor,
    actionContentColor: Color = SnackbarDefaults.actionColor,
    messageTextStyle: TextStyle? = null,
    actionLabelTextStyle: TextStyle? = null,
)
```

- `message` — the text to display.
- `modifier` — the Modifier applied to the snackbar.
- `actionLabel` — optional action button label; the action is shown only when both this and onAction are provided.
- `onAction` — optional action callback.
- `shape` — the snackbar container shape (defaults to FormaSnackbarDefaults.shape).
- `containerColor` — the snackbar's background color (defaults to the M3 default).
- `contentColor` — the color of the message text (defaults to the M3 default).
- `actionContentColor` — the color of the action button's label (defaults to SnackbarDefaults.actionColor — the same value FormaUI used before this was configurable, so the action keeps its look unless overridden).
- `messageTextStyle` — optional TextStyle override for message, merged on top of the M3 snackbar body style.
- `actionLabelTextStyle` — optional TextStyle override for actionLabel, merged on top of the M3 action-button label style.

**`FormaSnackbarHost`** — Hosts snackbars queued through a SnackbarHostState, rendering each as a FormaUI-styled FormaSnackbar (including its action button); place it in a Scaffold's snackbarHost slot.

```kotlin
fun FormaSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
)
```

- `hostState` — the SnackbarHostState whose current message is displayed.
- `modifier` — the Modifier applied to the host.

**Supporting API:**
- `FormaSnackbarDefaults` (object) — Default values used by FormaSnackbar; exposes shape (FormaUI's md corner tier).

**Variants:** Standard, Action

**Example:**

```kotlin
FormaSnackbar(
    message = "Message deleted.",
    actionLabel = "Undo",
    onAction = { /* undo */ },
)

// To surface transient messages, host queued snackbars:
val hostState = remember { SnackbarHostState() }
Scaffold(snackbarHost = { FormaSnackbarHost(hostState) }) { /* ... */ }
// elsewhere: scope.launch { hostState.showSnackbar("Saved.", actionLabel = "Undo") }
```

**Accessibility:** message and actionLabel render as real Text, so both are announced, and the action becomes a focusable button only when actionLabel and onAction are both supplied. Prefer queueing through SnackbarHostState + FormaSnackbarHost so Material 3 owns the show/dismiss timing, rather than composing FormaSnackbar directly.

---

## Slider

`dev.formaui.components.slider` · tier: prd

A FormaUI slider — lets the user select a value from a continuous or stepped range, delegating to Material 3's Slider.

**`FormaSlider`** — Stateless slider with hoisted state: the caller owns value and updates it in onValueChange; set steps to snap to discrete stops within valueRange.

```kotlin
fun FormaSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: SliderColors? = null,
)
```

- `value` — the current value; should sit within valueRange.
- `onValueChange` — called continuously with the new value as the user drags.
- `modifier` — the Modifier applied to the slider.
- `enabled` — whether the slider is interactive.
- `valueRange` — the inclusive range of selectable values (defaults to 0f..1f).
- `steps` — the number of discrete intermediate stops (0 = continuous).
- `onValueChangeFinished` — called once when the user finishes changing the value.
- `colors` — the slider colors (defaults to the M3 defaults, themed by FormaTheme).

**Variants:** Continuous, Stepped

**Example:**

```kotlin
var value by remember { mutableFloatStateOf(0.4f) }
FormaSlider(
    value = value,
    onValueChange = { value = it },
    modifier = Modifier.fillMaxWidth(),
)

// Stepped (discrete) slider:
var stepped by remember { mutableFloatStateOf(2f) }
FormaSlider(
    value = stepped,
    onValueChange = { stepped = it },
    valueRange = 0f..4f,
    steps = 3,
)
```

**Accessibility:** Exposes a 48dp touch target via Material 3's minimum interactive component size.

---

## BottomAppBar

`dev.formaui.components.bottomappbar` · tier: extra

A FormaUI bottom app bar — navigation and key actions anchored to the bottom of small screens, delegating to Material 3's BottomAppBar.

**`FormaBottomAppBar`** — Bottom bar of icon actions with an optional embedded FAB at the end.

```kotlin
fun FormaBottomAppBar(
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: (@Composable () -> Unit)? = null,
    containerColor: Color = FormaBottomAppBarDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
)
```

- `actions` — the bar's icon content, laid out in a RowScope (typically IconButtons).
- `modifier` — the Modifier applied to the bar.
- `floatingActionButton` — optional FAB embedded at the end of the bar (typically a FormaFloatingActionButton).
- `containerColor` — the background color of the bar.
- `contentColor` — the preferred content color, derived from containerColor by default.

**Supporting API:**
- `FormaBottomAppBarDefaults` (object) — Default values used by FormaBottomAppBar; exposes the default containerColor.

**Variants:** actions only, with embedded FAB

**Example:**

```kotlin
FormaBottomAppBar(
    actions = {
        FormaIconButton(onClick = { }) { Text("🔍") }
        FormaIconButton(onClick = { }) { Text("⋮") }
    },
    floatingActionButton = {
        FormaFloatingActionButton(onClick = { }) { Text("+") }
    },
)
```

**Accessibility:** The bar is icon-only, so supply a contentDescription on every icon in actions (and on the embedded floatingActionButton's) — without one they announce as unlabelled buttons. Touch targets come from the Material 3 icon buttons placed in actions.

---

## DropdownMenu

`dev.formaui.components.menu` · tier: extra

A FormaUI dropdown menu — a transient surface of choices anchored to a control, delegating to Material 3's DropdownMenu.

**`FormaDropdownMenu`** — Popup menu anchored to a sibling control; visibility is hoisted via expanded/onDismissRequest.

```kotlin
fun FormaDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
)
```

- `expanded` — whether the menu is currently shown.
- `onDismissRequest` — called when the user requests to dismiss the menu — an outside tap or the back gesture.
- `modifier` — the Modifier applied to the menu's content.
- `content` — the menu's items, laid out in a scrollable ColumnScope (typically FormaDropdownMenuItems).

**`FormaDropdownMenuItem`** — A single choice in a FormaDropdownMenu, delegating to Material 3's DropdownMenuItem.

```kotlin
fun FormaDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
)
```

- `text` — the item's label.
- `onClick` — called when the item is clicked. Not invoked while enabled is false. Callers typically also dismiss the menu from here.
- `modifier` — the Modifier applied to the item.
- `leadingIcon` — optional slot at the start of the item.
- `trailingIcon` — optional slot at the end of the item (can also hold Text for a keyboard shortcut hint).
- `enabled` — whether the item is interactive.

**Variants:** item with leading icon, item with trailing shortcut hint, disabled item

**Example:**

```kotlin
var expanded by remember { mutableStateOf(false) }
Box {
    FormaIconButton(onClick = { expanded = true }) { Text("⋮") }
    FormaDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        FormaDropdownMenuItem(text = "Share", onClick = { expanded = false })
        FormaDropdownMenuItem(text = "Delete", onClick = { expanded = false })
    }
}
```

**Accessibility:** The menu renders in its own popup and routes an outside tap or the back gesture to onDismissRequest. FormaDropdownMenuItem's text is a real Text, so each item announces its own label; treat leadingIcon/trailingIcon as decorative (contentDescription = null) since they repeat it.

---

## FloatingActionButton

`dev.formaui.components.fab` · tier: extra

A FormaUI floating action button — "Material 3 with better defaults"; one entry point covers all three Material 3 FAB sizes via size, with a default-on press-scale micro-interaction (shared Modifier.formaPressScale) on both FAB composables.

**`FormaFloatingActionButton`** — The screen's primary action, in Small/Regular/Large sizes; corners default to FormaUI's xl tier for a consistent silhouette across sizes.

```kotlin
fun FormaFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: FormaFabSize = FormaFabSize.Regular,
    shape: Shape? = null,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    interactionSource: MutableInteractionSource? = null,
    pressedScale: Float = FormaPressScaleDefaults.PressedScale,
    pressAnimationSpec: FiniteAnimationSpec<Float>? = FormaPressScaleDefaults.AnimationSpec,
    content: @Composable () -> Unit,
)
```

- `onClick` — called when the FAB is clicked.
- `modifier` — the Modifier applied to the FAB.
- `size` — the FAB's size (defaults to FormaFabSize.Regular).
- `shape` — the FAB's container shape (defaults to FormaFabDefaults.shape).
- `containerColor` — the background color of the FAB.
- `contentColor` — the preferred content color, derived from containerColor by default.
- `interactionSource` — the MutableInteractionSource for observing/emitting interactions. When null, one is remembered internally.
- `pressedScale` — the scale factor applied while the FAB is pressed (defaults to a subtle 0.97). Must be greater than 0.
- `pressAnimationSpec` — the animation used for the press-scale's release spring-back (defaults to a responsive spring; the press dip uses FormaPressScaleDefaults.DownAnimationSpec). Pass null to disable the press-scale entirely.
- `content` — the FAB's content, typically a single Icon.

**`FormaExtendedFloatingActionButton`** — A wider FAB pairing an icon with a text label; set expanded = false to collapse down to just the icon, with the width animating between states.

```kotlin
fun FormaExtendedFloatingActionButton(
    text: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    shape: Shape? = null,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    interactionSource: MutableInteractionSource? = null,
    pressedScale: Float = FormaPressScaleDefaults.PressedScale,
    pressAnimationSpec: FiniteAnimationSpec<Float>? = FormaPressScaleDefaults.AnimationSpec,
)
```

- `text` — the FAB's text label.
- `icon` — the FAB's leading icon.
- `onClick` — called when the FAB is clicked.
- `modifier` — the Modifier applied to the FAB.
- `expanded` — whether the FAB shows both icon and text (true) or collapses to just icon (false).
- `shape` — the FAB's container shape (defaults to FormaFabDefaults.shape).
- `containerColor` — the background color of the FAB.
- `contentColor` — the preferred content color, derived from containerColor by default.
- `interactionSource` — the MutableInteractionSource for observing/emitting interactions. When null, one is remembered internally.
- `pressedScale` — the scale factor applied while the FAB is pressed (defaults to a subtle 0.97). Must be greater than 0.
- `pressAnimationSpec` — the animation used for the press-scale's release spring-back (defaults to a responsive spring; the press dip uses FormaPressScaleDefaults.DownAnimationSpec). Pass null to disable the press-scale entirely.

**Supporting API:**
- `FormaFabSize` (enum) — The size of a FormaFloatingActionButton, mirroring Material 3's three FAB sizes: Small, Regular, Large.
- `FormaFabDefaults` (object) — Default values used by both FAB composables; exposes shape (FormaUI's xl corner tier, used across all sizes).

**Variants:** Small, Regular, Large, Extended

**Example:**

```kotlin
FormaFloatingActionButton(onClick = { /* create */ }) {
    Icon(Icons.Default.Add, contentDescription = "Add")
}

// Extended FAB with a label that can collapse on scroll:
var expanded by remember { mutableStateOf(true) }
FormaExtendedFloatingActionButton(
    text = "Compose",
    icon = { Icon(Icons.Default.Add, contentDescription = null) },
    onClick = { /* compose */ },
    expanded = expanded,
)
```

**Accessibility:** A FAB is icon-only, so its content Icon must carry a contentDescription naming the action — as must FormaExtendedFloatingActionButton's icon, whose text label is hidden while expanded is false. Role.Button semantics and the touch target come from Material 3; the press-scale is a graphicsLayer effect only, so it never shrinks the touch target.

---

## IconButton

`dev.formaui.components.iconbutton` · tier: extra

A FormaUI icon button — "Material 3 with better defaults"; one entry point covers all four Material 3 icon button variants via variant, with a default-on press-scale micro-interaction (shared Modifier.formaPressScale) using a deeper 0.92 dip.

**`FormaIconButton`** — One entry point for all four M3 icon button variants via variant; supply the content slot yourself, typically an Icon with a contentDescription.

```kotlin
fun FormaIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: FormaIconButtonVariant = FormaIconButtonVariant.Standard,
    enabled: Boolean = true,
    colors: IconButtonColors? = null,
    interactionSource: MutableInteractionSource? = null,
    pressedScale: Float = FormaIconButtonDefaults.PressedScale,
    pressAnimationSpec: FiniteAnimationSpec<Float>? = FormaPressScaleDefaults.AnimationSpec,
    content: @Composable () -> Unit,
)
```

- `onClick` — called when the button is clicked. Not invoked while enabled is false.
- `modifier` — the Modifier applied to the button.
- `variant` — the visual emphasis (defaults to FormaIconButtonVariant.Standard).
- `enabled` — whether the button is interactive.
- `colors` — the container/content colors. When null, the Material 3 default colors for the chosen variant are used.
- `interactionSource` — the MutableInteractionSource for observing/emitting interactions. When null, one is remembered internally.
- `pressedScale` — the scale factor applied while the button is pressed (defaults to a deeper 0.92 — small targets need a larger relative dip to read). Must be greater than 0.
- `pressAnimationSpec` — the animation used for the press-scale's release spring-back (defaults to a responsive spring; the press dip uses FormaPressScaleDefaults.DownAnimationSpec). Pass null to disable the press-scale entirely.
- `content` — the button's content — typically a single Icon with a contentDescription.

**Supporting API:**
- `FormaIconButtonVariant` (enum) — The visual emphasis of a FormaIconButton, mirroring Material 3's four icon button variants: Standard, Filled, Tonal, Outlined.
- `FormaIconButtonDefaults` (object) — Defaults for FormaIconButton: PressedScale (0.92 — a deeper press-scale dip than larger controls, so the feedback reads at icon size).

**Variants:** Standard, Filled, Tonal, Outlined

**Example:**

```kotlin
FormaIconButton(onClick = { /* favorite */ }) {
    Icon(Icons.Default.Star, contentDescription = "Favorite")
}

FormaIconButton(onClick = { /* menu */ }, variant = FormaIconButtonVariant.Filled) {
    Icon(Icons.Default.Menu, contentDescription = "Open menu")
}
```

**Accessibility:** 48dp minimum touch target and Role.Button semantics inherited from the underlying Material 3 icon button; content should be an Icon with a contentDescription since the button carries no text label of its own.

---

## NavigationDrawer

`dev.formaui.components.navigation` · tier: extra

A FormaUI modal navigation drawer — an off-canvas panel of destinations that slides in over content and is dismissed with a scrim tap, delegating to Material 3's ModalNavigationDrawer.

**`FormaModalNavigationDrawer`** — Modal drawer that auto-wraps drawerContent in a ModalDrawerSheet; open/closed state is hoisted via DrawerState.

```kotlin
fun FormaModalNavigationDrawer(
    drawerState: DrawerState,
    drawerContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    gesturesEnabled: Boolean = true,
    scrimColor: Color = DrawerDefaults.scrimColor,
    drawerContainerColor: Color = DrawerDefaults.modalContainerColor,
    drawerContentColor: Color = contentColorFor(drawerContainerColor),
    content: @Composable () -> Unit,
)
```

- `drawerState` — the drawer's hoisted open/closed state (e.g. rememberDrawerState(DrawerValue.Closed)).
- `drawerContent` — the drawer's items, laid out in a ColumnScope inside a ModalDrawerSheet (typically FormaNavigationDrawerItems).
- `modifier` — the Modifier applied to the drawer.
- `gesturesEnabled` — whether the drawer can be opened/closed by a swipe gesture.
- `scrimColor` — the color of the scrim that obscures content while the drawer is open (defaults to the M3 default scrim).
- `drawerContainerColor` — the background color of the drawer sheet (defaults to the M3 modal drawer container, themed by FormaTheme).
- `drawerContentColor` — the preferred content color inside the drawer sheet (defaults to the color matching drawerContainerColor).
- `content` — the screen content behind the drawer.

**`FormaNavigationDrawerItem`** — A single destination row in the drawer, with optional leading icon and trailing text badge (e.g. an unread count).

```kotlin
fun FormaNavigationDrawerItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    badge: String? = null,
    colors: NavigationDrawerItemColors? = null,
    labelTextStyle: TextStyle? = null,
    badgeTextStyle: TextStyle? = null,
)
```

- `label` — the item's text label.
- `selected` — whether this item is the current destination.
- `onClick` — called when the item is tapped.
- `modifier` — the Modifier applied to the item.
- `icon` — optional leading icon (supply a contentDescription on it for accessibility).
- `badge` — optional trailing text (e.g. an unread count) shown at the item's end.
- `colors` — the item colors — text, icon, badge, and selection-container colors for the selected and unselected states. Defaults to the M3 defaults, themed by FormaTheme.
- `labelTextStyle` — optional TextStyle override for label, merged on top of the M3 label style.
- `badgeTextStyle` — optional TextStyle override for badge, merged on top of the M3 badge style.

**Variants:** modal

**Example:**

```kotlin
val drawerState = rememberDrawerState(DrawerValue.Open)
var selected by remember { mutableIntStateOf(0) }
FormaModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
        FormaNavigationDrawerItem(label = "Inbox", selected = selected == 0, onClick = { selected = 0 }, badge = "24")
        FormaNavigationDrawerItem(label = "Sent", selected = selected == 1, onClick = { selected = 1 })
    },
) {
    Text("Mail content behind the drawer")
}
```

**Accessibility:** Item icons should carry a contentDescription for accessibility (noted on the icon param); dismissible via scrim tap and (when gesturesEnabled) swipe.

---

## NavigationRail

`dev.formaui.components.navigation` · tier: extra

A FormaUI navigation rail — top-level destination switching for tablet/desktop-width screens, delegating to Material 3's NavigationRail.

**`FormaNavigationRail`** — Vertical rail pinned to the start edge, with an optional header slot (typically a FAB or logo) above the items.

```kotlin
fun FormaNavigationRail(
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationRailDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    header: @Composable (ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
)
```

- `modifier` — the Modifier applied to the rail.
- `containerColor` — the rail's background color (defaults to the M3 navigation-rail container, themed by FormaTheme).
- `contentColor` — the preferred content color for items (defaults to the color matching containerColor).
- `header` — optional content shown above the items (typically a FAB or a logo).
- `content` — the rail's items, laid out in a ColumnScope (typically 3-7 FormaNavigationRailItems).

**`FormaNavigationRailItem`** — A single rail destination with built-in per-item badge support (numeric badgeCount or dot via showBadgeDot); takes no scope receiver.

```kotlin
fun FormaNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    badgeCount: Int? = null,
    showBadgeDot: Boolean = false,
    alwaysShowLabel: Boolean = true,
    colors: NavigationRailItemColors? = null,
    labelTextStyle: TextStyle? = null,
)
```

- `selected` — whether this item is the current destination.
- `onClick` — called when the item is tapped.
- `icon` — the item's icon (supply a contentDescription on it for accessibility).
- `modifier` — the Modifier applied to the item.
- `label` — optional text label shown under the icon.
- `enabled` — whether the item is interactive.
- `badgeCount` — optional unread count to show as a numeric badge on the icon.
- `showBadgeDot` — when true and badgeCount is null, shows a dot badge on the icon.
- `alwaysShowLabel` — whether the label is shown even when the item is unselected.
- `colors` — the item colors — icon, label, and selection-indicator colors for the selected, unselected, and disabled states. Defaults to the M3 defaults, themed by FormaTheme.
- `labelTextStyle` — optional TextStyle override for label, merged on top of the M3 label style.

**Variants:** with header, item with numeric badge, item with dot badge

**Example:**

```kotlin
var selected by remember { mutableIntStateOf(0) }
FormaNavigationRail(
    header = { Text("+") },
) {
    FormaNavigationRailItem(selected = selected == 0, onClick = { selected = 0 }, icon = { Text("🏠") }, label = "Home")
    FormaNavigationRailItem(selected = selected == 1, onClick = { selected = 1 }, icon = { Text("🔔") }, label = "Alerts", badgeCount = 12)
    FormaNavigationRailItem(selected = selected == 2, onClick = { selected = 2 }, icon = { Text("👤") }, label = "Profile", showBadgeDot = true)
}
```

**Accessibility:** 48dp touch target and selection semantics come from Material 3's NavigationRailItem; supply a contentDescription on each item's icon.

---

## SearchBar

`dev.formaui.components.search` · tier: extra

A FormaUI search bar — "Material 3 with better defaults", wrapping Material 3's SearchBar/DockedSearchBar behind one entry point whose variant switches between the docked and full-screen expansion surfaces.

**`FormaSearchBar`** — Search input that expands into a Docked results panel or a FullScreen overlay; query and expansion state are both hoisted.

```kotlin
fun FormaSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    variant: FormaSearchBarVariant = FormaSearchBarVariant.Docked,
    enabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape? = null,
    colors: SearchBarColors? = null,
    tonalElevation: Dp = FormaSearchBarDefaults.TonalElevation,
    shadowElevation: Dp = FormaSearchBarDefaults.ShadowElevation,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable ColumnScope.() -> Unit = {},
)
```

- `query` — the current query text shown in the input field.
- `onQueryChange` — called with the updated text as the user types.
- `onSearch` — called with the current query when the input service triggers the search (IME) action.
- `expanded` — whether the search bar is expanded and showing content.
- `onExpandedChange` — called with the requested new expanded state — e.g. true when the input field gains focus, false on dismiss request or back navigation.
- `modifier` — the Modifier applied to the search bar in its collapsed layout position.
- `variant` — which Material 3 surface to expand into (defaults to FormaSearchBarVariant.Docked).
- `enabled` — whether the input field is interactive.
- `placeholder` — the placeholder shown in the input field when query is empty.
- `leadingIcon` — the input field's leading icon slot, typically a search glyph.
- `trailingIcon` — the input field's trailing icon slot, e.g. a clear button shown when query is not empty.
- `shape` — the collapsed search bar's container shape. When null, the Material 3 default for the chosen variant is used (FormaSearchBarDefaults.dockedShape or FormaSearchBarDefaults.fullScreenCollapsedShape).
- `colors` — the container/divider/input-field colors. When null, the Material 3 default colors are used.
- `tonalElevation` — the tonal elevation applied when colors' container color is the surface color (defaults to FormaSearchBarDefaults.TonalElevation).
- `shadowElevation` — the shadow elevation below the collapsed search bar (defaults to FormaSearchBarDefaults.ShadowElevation).
- `interactionSource` — the MutableInteractionSource for observing/emitting interactions on the input field. When null, one is remembered internally.
- `content` — the search results (or suggestions) shown below the input field while expanded, laid out in a ColumnScope.

**Supporting API:**
- `FormaSearchBarVariant` (enum) — The surface the search bar expands into; values: Docked, FullScreen.
- `FormaSearchBarDefaults` (object) — Default values: dockedShape, fullScreenCollapsedShape, TonalElevation, ShadowElevation.

**Variants:** Docked, FullScreen

**Example:**

```kotlin
var query by remember { mutableStateOf("") }
var expanded by remember { mutableStateOf(false) }
FormaSearchBar(
    query = query,
    onQueryChange = { query = it },
    onSearch = { expanded = false },
    expanded = expanded,
    onExpandedChange = { expanded = it },
    variant = FormaSearchBarVariant.Docked,
    placeholder = { Text("Search contacts") },
    leadingIcon = { Text("🔍") },
) {
    Text("Ada Lovelace")
}
```

**Accessibility:** The input field carries Material 3's text-field semantics, and expansion stays hoisted — onExpandedChange fires both when the field gains focus and on back navigation, so the caller owns the collapse. A search-glyph leadingIcon is decorative (contentDescription = null); give trailingIcon a description when it is an action such as clear.

---

## SegmentedButton

`dev.formaui.components.segmentedbutton` · tier: extra

A FormaUI segmented button row — a set of connected, mutually-styled buttons for view switching or option selection, delegating to Material 3's SingleChoiceSegmentedButtonRow / MultiChoiceSegmentedButtonRow.

**`FormaSegmentedButtonRow`** — One entry point for both Material 3 selection modes: multiSelect = false for single-choice, true for per-segment toggles.

```kotlin
fun FormaSegmentedButtonRow(
    multiSelect: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable FormaSegmentedButtonRowScope.() -> Unit,
)
```

- `multiSelect` — false for mutually-exclusive (single-choice) selection, true for independent per-segment toggles (multi-choice).
- `modifier` — the Modifier applied to the row.
- `content` — the row's segments, typically FormaSegmentedButtons.

**`FormaSegmentedButton`** — Single-choice segment (RadioButton semantics) — extension on FormaSegmentedButtonRowScope; use in rows with multiSelect = false.

```kotlin
fun FormaSegmentedButton(
    selected: Boolean,
    onClick: () -> Unit,
    index: Int,
    count: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit = { SegmentedButtonDefaults.Icon(selected) },
    label: @Composable () -> Unit,
)
```

- `selected` — whether this segment is the currently selected one.
- `onClick` — called when the segment is clicked. Not invoked while enabled is false.
- `index` — this segment's position within the row (0-based) — used to compute the correct start/middle/end corner shape.
- `count` — the total number of segments in the row — used to compute the correct start/middle/end corner shape.
- `modifier` — the Modifier applied to the segment.
- `enabled` — whether the segment is interactive.
- `icon` — the segment's leading icon slot. Defaults to Material 3's crossfading checkmark, shown only while selected.
- `label` — the segment's content, typically a Text.

**`FormaSegmentedButton`** — Multi-choice segment (independent toggle semantics) — extension on FormaSegmentedButtonRowScope; use in rows with multiSelect = true.

```kotlin
fun FormaSegmentedButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    index: Int,
    count: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit = { SegmentedButtonDefaults.Icon(checked) },
    label: @Composable () -> Unit,
)
```

- `checked` — whether this segment is currently checked.
- `onCheckedChange` — called with the requested new checked state when the segment is clicked. Not invoked while enabled is false.
- `index` — this segment's position within the row (0-based) — used to compute the correct start/middle/end corner shape.
- `count` — the total number of segments in the row — used to compute the correct start/middle/end corner shape.
- `modifier` — the Modifier applied to the segment.
- `enabled` — whether the segment is interactive.
- `icon` — the segment's leading icon slot. Defaults to Material 3's crossfading checkmark, shown only while checked.
- `label` — the segment's content, typically a Text.

**Supporting API:**
- `FormaSegmentedButtonRowScope` (class) — Interface (scope) extending both SingleChoiceSegmentedButtonRowScope and MultiChoiceSegmentedButtonRowScope so one content lambda type serves both selection modes.

**Variants:** single-select, multi-select

**Example:**

```kotlin
var selectedPeriod by remember { mutableStateOf(0) }
val periods = listOf("Day", "Week", "Month")
FormaSegmentedButtonRow(multiSelect = false) {
    periods.forEachIndexed { index, label ->
        FormaSegmentedButton(
            selected = selectedPeriod == index,
            onClick = { selectedPeriod = index },
            index = index,
            count = periods.size,
            label = { Text(label) },
        )
    }
}
```

**Accessibility:** Single-select segments carry RadioButton semantics and multi-select segments carry toggle semantics, via the correctly-scoped Material 3 SegmentedButton overloads.

---

## TabRow

`dev.formaui.components.tabs` · tier: extra

A FormaUI tab row — switches between a small number of related content destinations, delegating to Material 3's PrimaryTabRow/SecondaryTabRow (or their scrollable equivalents).

**`FormaTabRow`** — Stateless tab row covering both indicator emphases (variant) and fixed vs. scrollable layout (scrollable) in one entry point.

```kotlin
fun FormaTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    variant: FormaTabRowVariant = FormaTabRowVariant.Primary,
    scrollable: Boolean = false,
    tabs: @Composable () -> Unit,
)
```

- `selectedTabIndex` — the index of the currently selected tab, used to position the active indicator.
- `modifier` — the Modifier applied to the row.
- `variant` — the indicator style (defaults to FormaTabRowVariant.Primary).
- `scrollable` — when true, tabs are laid out from the start edge and scroll if they overflow — use this for a large or dynamic number of tabs. When false (the default), tabs are fixed and evenly spaced across the row's width — use this for a small, stable set of tabs.
- `tabs` — the row's tabs, typically FormaTabs.

**`FormaTab`** — A single tab; delegates to Material 3's Tab, or to LeadingIconTab automatically when both text and icon are supplied.

```kotlin
fun FormaTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    selectedContentColor: Color? = null,
    unselectedContentColor: Color? = null,
    interactionSource: MutableInteractionSource? = null,
)
```

- `selected` — whether this tab is the currently active destination.
- `onClick` — called when the tab is clicked. Not invoked while enabled is false.
- `modifier` — the Modifier applied to the tab.
- `text` — optional text label.
- `icon` — optional icon. Should be 24dp.
- `enabled` — whether the tab is interactive.
- `selectedContentColor` — the color for the tab's content (and its ripple) when selected. When null, LocalContentColor is used.
- `unselectedContentColor` — the color for the tab's content when not selected. When null, defaults to selectedContentColor.
- `interactionSource` — the MutableInteractionSource for observing/emitting interactions. When null, one is remembered internally.

**Supporting API:**
- `FormaTabRowVariant` (enum) — Indicator style; values: Primary (bold pill indicator, top-level navigation), Secondary (slim underline, views within one destination).

**Variants:** primary, secondary, fixed, scrollable

**Example:**

```kotlin
var selectedTab by remember { mutableStateOf(0) }
FormaTabRow(selectedTabIndex = selectedTab) {
    FormaTab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Chat") })
    FormaTab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Calls") })
}
```

**Accessibility:** Role.Tab selection semantics and the 48dp touch target come from Material 3's PrimaryTabRow/SecondaryTabRow (and their scrollable equivalents). A FormaTab given only an icon and no text has no accessible label — supply a contentDescription on the icon in that case.

---

## Tooltip

`dev.formaui.components.tooltip` · tier: extra

A FormaUI tooltip — "Material 3 with better defaults", wrapping Material 3's TooltipBox + PlainTooltip/RichTooltip.

**`FormaTooltip`** — Tooltip anchored to its content slot, triggered on long-press/hover; Plain or Rich (title + action) via variant, caret on by default.

```kotlin
fun FormaTooltip(
    text: String,
    modifier: Modifier = Modifier,
    variant: FormaTooltipVariant = FormaTooltipVariant.Plain,
    state: TooltipState = rememberTooltipState(),
    title: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    showCaret: Boolean = true,
    positionProvider: PopupPositionProvider? = null,
    onDismissRequest: (() -> Unit)? = null,
    enableUserInput: Boolean = true,
    colors: RichTooltipColors? = null,
    containerColor: Color = TooltipDefaults.plainTooltipContainerColor,
    contentColor: Color = TooltipDefaults.plainTooltipContentColor,
    textStyle: TextStyle? = null,
    content: @Composable () -> Unit,
)
```

- `text` — the tooltip's message.
- `modifier` — the Modifier applied to the anchor (content).
- `variant` — the tooltip surface (defaults to FormaTooltipVariant.Plain).
- `state` — hoisted visibility state; controls and reports whether the tooltip is currently shown (defaults to a freshly remembered, non-persistent TooltipState).
- `title` — an optional title shown above text. Only used when variant is FormaTooltipVariant.Rich; ignored for FormaTooltipVariant.Plain.
- `action` — an optional action (typically a TextButton) shown below text. Only used when variant is FormaTooltipVariant.Rich; ignored for Plain. When non-null, the tooltip becomes persistent-friendly: Material 3 keeps it focusable so accessibility services can reach the action.
- `showCaret` — whether a caret pointing at the anchor is drawn on the tooltip container. FormaUI defaults this to true (Material 3's own default is no caret) since it makes the anchor relationship unambiguous.
- `positionProvider` — controls where the tooltip is placed relative to content. When null, it is placed above the anchor via TooltipDefaults.rememberTooltipPositionProvider.
- `onDismissRequest` — called when the user clicks outside the tooltip while it is shown. When null, Material 3's default dismiss-on-outside-click behavior applies.
- `enableUserInput` — whether long-press and hover on content trigger the tooltip through state. Set to false if you drive state entirely programmatically.
- `colors` — the container/content/title/action colors used by FormaTooltipVariant.Rich. When null, the Material 3 default rich tooltip colors are used. Has no effect on FormaTooltipVariant.Plain.
- `containerColor` — the tooltip's background color for FormaTooltipVariant.Plain (defaults to the M3 plain-tooltip container). No effect on Rich — use colors there.
- `contentColor` — the tooltip's content color for FormaTooltipVariant.Plain (defaults to the M3 plain-tooltip content color). No effect on Rich — use colors there.
- `textStyle` — optional TextStyle override for the tooltip's body text (both variants), merged on top of the M3 tooltip body style.
- `content` — the anchor content that the tooltip attaches to.

**Supporting API:**
- `FormaTooltipVariant` (enum) — Visual weight; values: Plain (single line of supporting text), Rich (larger surface with optional title and action).
- `FormaTooltipDefaults` (object) — Default values: caretShape, plainMaxWidth, richMaxWidth.

**Variants:** Plain, Rich

**Example:**

```kotlin
val tooltipState = rememberTooltipState()
FormaTooltip(
    text = "Search",
    state = tooltipState,
) {
    FormaIconButton(onClick = { }) { Text("🔍") }
}
```

**Accessibility:** When a Rich tooltip has a non-null action, Material 3 keeps the tooltip focusable so accessibility services can reach the action; triggered by long-press or hover on the anchor.

---

## TopAppBar

`dev.formaui.components.topappbar` · tier: extra

A FormaUI top app bar — "Material 3 with better defaults"; one entry point covers all four Material 3 top app bar sizes via variant.

**`FormaTopAppBar`** — One entry point for all four M3 top app bar sizes via variant; pair Medium/Large with a scrollBehavior for the standard collapsing-header behavior (call sites also need @OptIn(ExperimentalMaterial3Api::class) because TopAppBarScrollBehavior is still experimental in M3).

```kotlin
fun FormaTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    variant: FormaTopAppBarVariant = FormaTopAppBarVariant.Small,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors? = null,
)
```

- `title` — the bar's title text.
- `modifier` — the Modifier applied to the bar.
- `variant` — the bar's layout (defaults to FormaTopAppBarVariant.Small).
- `navigationIcon` — optional slot at the start of the bar (typically an IconButton, e.g. FormaIconButton, for a back or menu action).
- `actions` — the actions displayed at the end of the bar, laid out in a RowScope (typically IconButtons).
- `scrollBehavior` — an optional TopAppBarScrollBehavior that ties this bar's height/color changes to a scrolling content's NestedScrollConnection.
- `colors` — the container/content colors. When null, the Material 3 default colors for the chosen variant are used.

**Supporting API:**
- `FormaTopAppBarVariant` (enum) — The layout of a FormaTopAppBar, mirroring Material 3's four top app bar sizes: Small, CenterAligned, Medium, Large.

**Variants:** Small, CenterAligned, Medium, Large

**Example:**

```kotlin
FormaTopAppBar(
    title = "Inbox",
    variant = FormaTopAppBarVariant.Small,
    navigationIcon = {
        FormaIconButton(onClick = { /* back */ }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    },
)
```

**Accessibility:** title is a real Text and is announced as the bar's label; navigationIcon and actions are icon slots, so supply a contentDescription on each (e.g. "Back", "More options"). Touch targets come from the Material 3 icon buttons placed in those slots.

---

## BarChart

`dev.formaui.components.chart` · tier: extra

A FormaUI bar chart — vertical bars with rounded top corners, drawn with pure Compose Canvas (no chart-library dependency); gridlines, category labels, value labels, and a one-shot entry animation are all built in.

**`FormaBarChart`** — Vertical rounded bars scaled against a "nice" auto-computed (or explicit) y-axis maximum, with optional gridlines, category labels, value labels, and an entry animation; exposes an auto-generated data summary as its content description.

```kotlin
fun FormaBarChart(
    entries: List<FormaChartEntry>,
    modifier: Modifier = Modifier,
    barColor: Color = Color.Unspecified,
    maxValue: Float? = null,
    showValueLabels: Boolean = false,
    showCategoryLabels: Boolean = true,
    showGridLines: Boolean = true,
    barCornerRadius: Dp = FormaChartDefaults.BarCornerRadius,
    valueFormatter: (Float) -> String = FormaChartDefaults.ValueFormatter,
    animationSpec: FiniteAnimationSpec<Float>? = FormaChartDefaults.EntryAnimationSpec,
    contentDescription: String? = null,
)
```

- `entries` — the data to plot, one bar per entry, in order. Values must be finite and non-negative. An empty list renders an empty (but described) chart area.
- `modifier` — the Modifier applied to the chart (it fills the available width at a minimum height of FormaChartDefaults.MinChartHeight).
- `barColor` — the fill color for all bars (defaults to the theme's primary). A FormaChartEntry.color that is specified overrides this for that bar.
- `maxValue` — the value the y-axis tops out at. When null, a "nice" rounded maximum is computed from the data. Must be finite and positive when supplied; values above it are clipped to the top of the plot.
- `showValueLabels` — whether to draw each bar's formatted value above it. Labels use FormaChartDefaults.valueLabelStyle (tabular figures).
- `showCategoryLabels` — whether to draw each entry's label below its bar. Labels use FormaChartDefaults.axisLabelStyle, centered per bar and ellipsized to the bar's slot width.
- `showGridLines` — whether to draw horizontal gridlines dividing the plot into FormaChartDefaults.GridLineCount bands, in FormaChartDefaults.gridLineColor.
- `barCornerRadius` — the radius of each bar's top corners (defaults to 4dp).
- `valueFormatter` — formats a value for value labels and the auto-generated accessibility summary.
- `animationSpec` — the entry animation for bar growth, or null to render a static final frame (deterministic in tests and screenshots).
- `contentDescription` — an explicit accessibility description; when null a summary of the data is generated automatically.

**Supporting API:**
- `FormaChartEntry` (data class) — One data point shared by all FormaUI charts (label, value, optional explicit color). Charts are stateless and redraw when the caller passes a structurally different list — an equal-but-new list never replays the entry animation.
- `FormaChartDefaults` (object) — Defaults shared by all FormaUI charts: MinChartHeight (180dp), MinDonutSize (160dp), DonutStrokeWidth (24dp), LineStrokeWidth (2dp), LinePointRadius (3dp), BarCornerRadius (4dp), LegendSwatchSize (12dp), GridLineCount (4), seriesColors (a six-color M3 palette with error last), gridLineColor, valueLabelStyle (tabular figures), axisLabelStyle, ValueFormatter, and EntryAnimationSpec (an 800ms tween).
- `FormaChartLegend` (composable) — A wrapping row of color swatches and labels, one item per FormaChartEntry. Charts never embed a legend — place one next to the chart with the same entries (and, for a donut, the same palette) so swatches resolve to the chart's colors; an optional valueFormatter appends each entry's formatted value. Labels are real Text, so the legend is natively accessible.

**Variants:** Default, Value labels, Custom colors, No gridlines, Static (no animation)

**Example:**

```kotlin
FormaBarChart(
    entries = listOf(
        FormaChartEntry("Jan", 12f),
        FormaChartEntry("Feb", 32f),
        FormaChartEntry("Mar", 21f),
    ),
    showValueLabels = true,
)
```

**Accessibility:** Exposes an auto-generated data summary (e.g. "Bar chart with 3 categories. Jan: 12. …") as its semantics content description — override with contentDescription; empty entries describe themselves as "Bar chart with no data".

---

## DonutChart

`dev.formaui.components.chart` · tier: extra

A FormaUI donut chart — proportional stroked arc segments around an open center slot, drawn with pure Compose Canvas (no chart-library dependency); segments cycle the shared series palette and sweep in clockwise on entry.

**`FormaDonutChart`** — Proportional arc segments separated by small gaps around an open center, cycling FormaChartDefaults.seriesColors, with a centerContent slot (e.g. a total) and an auto-generated percentage summary as its content description; empty or all-zero data draws a neutral track ring instead of crashing.

```kotlin
fun FormaDonutChart(
    entries: List<FormaChartEntry>,
    modifier: Modifier = Modifier,
    segmentColors: List<Color>? = null,
    strokeWidth: Dp = FormaChartDefaults.DonutStrokeWidth,
    strokeCap: StrokeCap = StrokeCap.Round,
    startAngle: Float = -90f,
    segmentGapDegrees: Float = 2f,
    animationSpec: FiniteAnimationSpec<Float>? = FormaChartDefaults.EntryAnimationSpec,
    contentDescription: String? = null,
    centerContent: @Composable () -> Unit = {},
)
```

- `entries` — the data to plot, one segment per entry, clockwise in order. Values must be finite and non-negative; each segment's sweep is proportional to its share of the total.
- `modifier` — the Modifier applied to the chart. It keeps a 1:1 aspect ratio at a minimum size of FormaChartDefaults.MinDonutSize; size it explicitly (e.g. Modifier.size(200.dp)).
- `segmentColors` — the palette cycled for segments whose entry has no explicit FormaChartEntry.color; null uses FormaChartDefaults.seriesColors.
- `strokeWidth` — the thickness of the donut's ring (defaults to 24dp).
- `strokeCap` — the cap style for each segment's ends: StrokeCap.Round (the default) gives softly rounded ends, insetting each drawn arc by the caps' angular overhang so gaps stay visible and segments land on their true bounds (a segment too small to fit its two caps renders as a dot); StrokeCap.Butt gives flat, squared ends. Irrelevant on a closed ring (a lone visible segment, or the zero-total track ring).
- `startAngle` — the angle the first segment starts at, in degrees clockwise from 3 o'clock; the default starts at 12 o'clock.
- `segmentGapDegrees` — the angular gap between adjacent segments, in degrees. Ignored when only one segment is visible. Must be finite and non-negative.
- `animationSpec` — the entry animation for the clockwise reveal, or null to render a static final frame.
- `contentDescription` — an explicit accessibility description; when null a percentage summary of the data is generated automatically.
- `centerContent` — a slot composed in the donut's center hole (e.g. a total or a caption). Centered but not clipped, so keep it smaller than the hole; its semantics stay independently accessible next to the chart summary.

**Supporting API:**
- `FormaChartEntry` (data class) — The shared chart data point — see the bar-chart entry for the full shared chart API (FormaChartEntry, FormaChartDefaults, FormaChartLegend).

**Variants:** Default, Custom palette, Center content, With legend, Zero-total track ring

**Example:**

```kotlin
FormaDonutChart(
    entries = listOf(
        FormaChartEntry("Rent", 850f),
        FormaChartEntry("Groceries", 420f),
        FormaChartEntry("Transport", 210f),
    ),
    centerContent = { Text("1480") },
)
```

**Accessibility:** Exposes an auto-generated integer-percentage summary (e.g. "Donut chart with 3 segments. Rent: 57 percent. …") as its semantics content description — override with contentDescription; empty entries describe themselves as "Donut chart with no data", and the centerContent slot's semantics stay independently accessible.

---

## LineChart

`dev.formaui.components.chart` · tier: extra

A FormaUI line chart — a single series plotted as a smooth (or straight) trend line with an optional area fill, point markers, gridlines, and x-axis labels, drawn with pure Compose Canvas (no chart-library dependency). Accepts negative values.

**`FormaLineChart`** — Plots a List<Float> as a trend line: smooth horizontally-clamped cubic curves (no vertical overshoot) or straight segments, a gradient area fill, optional point markers and x labels, and a leading-edge sweep entry animation; degenerate data (empty, single point, flat series) renders gracefully.

```kotlin
fun FormaLineChart(
    values: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color.Unspecified,
    strokeWidth: Dp = FormaChartDefaults.LineStrokeWidth,
    minValue: Float? = null,
    maxValue: Float? = null,
    smooth: Boolean = true,
    fillArea: Boolean = true,
    showPoints: Boolean = false,
    showGridLines: Boolean = true,
    xLabels: List<String> = emptyList(),
    valueFormatter: (Float) -> String = FormaChartDefaults.ValueFormatter,
    animationSpec: FiniteAnimationSpec<Float>? = FormaChartDefaults.EntryAnimationSpec,
    contentDescription: String? = null,
)
```

- `values` — the series to plot, in order, spread evenly across the width. Values must be finite (negative values are allowed). An empty list renders an empty (but described) chart area.
- `modifier` — the Modifier applied to the chart (it fills the available width at a minimum height of FormaChartDefaults.MinChartHeight).
- `lineColor` — the color of the line, its points, and the area fill (defaults to the theme's primary).
- `strokeWidth` — the thickness of the line (defaults to 2dp).
- `minValue` — the value the y-axis bottoms out at. When null, the data's own minimum is used. Must be finite when supplied; values below it are clipped to the plot's bottom edge.
- `maxValue` — the value the y-axis tops out at. When null, the data's own maximum is used. Must be finite (and greater than minValue when both are supplied); values above it are clipped to the plot's top edge.
- `smooth` — whether segments are smoothed cubic curves or straight polyline segments. Smoothing clamps control points horizontally to the midpoint between neighboring x positions, so the curve never overshoots a value vertically.
- `fillArea` — whether to fill the area under the line with a vertical gradient of lineColor fading to transparent at the plot floor.
- `showPoints` — whether to draw a FormaChartDefaults.LinePointRadius circle marker at each data point. A single-value dataset always draws its point (there is no segment to draw).
- `showGridLines` — whether to draw horizontal gridlines dividing the plot into FormaChartDefaults.GridLineCount bands, in FormaChartDefaults.gridLineColor.
- `xLabels` — optional x-axis labels drawn under the plot in FormaChartDefaults.axisLabelStyle, paired index-by-index with values and centered under their points; labels beyond the point count are ignored. Empty reserves no label band.
- `valueFormatter` — formats a value for the auto-generated accessibility summary.
- `animationSpec` — the entry animation for the leading-edge sweep, or null to render a static final frame.
- `contentDescription` — an explicit accessibility description; when null a summary of the data is generated automatically.

**Supporting API:**
- `FormaChartDefaults` (object) — Shared chart defaults (LineStrokeWidth, LinePointRadius, GridLineCount, EntryAnimationSpec, ValueFormatter, …) — see the bar-chart entry for the full shared chart API.

**Variants:** Smooth with area fill, Straight polyline, Point markers, Negative values, X-axis labels, Flat / single point

**Example:**

```kotlin
FormaLineChart(
    values = listOf(4f, 9f, 6f, 14f, 11f),
    showPoints = true,
    xLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri"),
)
```

**Accessibility:** Exposes an auto-generated summary of point count, min–max range, and latest value (e.g. "Line chart with 5 points. Range 4 to 14. Latest value 11.") as its semantics content description — override with contentDescription; empty values describe themselves as "Line chart with no data".

---

## DatePickerSheet

`dev.formaui.components.datepicker` · tier: extra

A FormaUI date picker presented in a modal bottom sheet — Material 3's inline DatePicker hosted in a FormaBottomSheet instead of the M3 DatePickerDialog, with hoisted M3 state, confirm/dismiss button slots, and the calendar-to-text-input mode toggle.

**`FormaDatePickerSheet`** — Hosts M3's stable DatePicker in a FormaBottomSheet above an end-aligned button row. State is the caller's DatePickerState and the component wires nothing to the button slots — the confirm slot reads state.selectedDateMillis itself (so it can disable itself until a date is picked). The default colors make the picker's own container transparent so it sits directly on the sheet surface.

```kotlin
fun FormaDatePickerSheet(
    onDismissRequest: () -> Unit,
    state: DatePickerState,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: (@Composable () -> Unit)? = null,
    showModeToggle: Boolean = true,
    skipPartiallyExpanded: Boolean = true,
    shape: Shape? = null,
    colors: DatePickerColors? = null,
)
```

- `onDismissRequest` — called when the sheet is dismissed (scrim tap, back press, or swipe down) — hide the sheet by no longer composing it. There is no separate cancel callback; treat a dismiss as closed-without-confirming.
- `state` — the hoisted Material 3 state; create it with rememberDatePickerState. Read state.selectedDateMillis (UTC epoch millis, Long?) in the confirm slot's click handler. The picker's locale is captured when the state is created (Material 3 behavior).
- `confirmButton` — the primary action slot (required), typically a FormaButton. The component attaches no click handling or label — the slot owns both.
- `modifier` — the Modifier applied to the sheet.
- `dismissButton` — the optional secondary/cancel action slot, shown before confirmButton.
- `showModeToggle` — whether the picker shows Material 3's calendar-to-text-input mode toggle.
- `skipPartiallyExpanded` — whether the sheet skips the half-expanded state and opens fully. Defaults to true — deliberately unlike FormaBottomSheet — because the half-expanded state clips the calendar grid.
- `shape` — the sheet container shape. When null, Material 3's top-rounded default is used.
- `colors` — the DatePickerColors for the picker. When null, Material 3's defaults are used with a transparent picker container so the picker sits directly on the sheet surface (avoiding a container-on-container seam). A supplied value is passed through untouched.

**Supporting API:**
- `rememberDatePickerState` (function (Material 3)) — The component defines no state type of its own — callers create and own the picker state via Material 3's rememberDatePickerState (initial date, selectableDates, yearRange, initialDisplayMode, saveability) and read selectedDateMillis from it.

**Variants:** Calendar mode, Text-input mode (initialDisplayMode or the mode toggle), With dismiss button, Mode toggle hidden, Custom colors

**Example:**

```kotlin
val state = rememberDatePickerState()
if (open) {
    FormaDatePickerSheet(
        onDismissRequest = { open = false },
        state = state,
        confirmButton = {
            FormaButton(
                onClick = { onPicked(state.selectedDateMillis); open = false },
                enabled = state.selectedDateMillis != null,
            ) { Text("OK") }
        },
    )
}
```

**Accessibility:** Inherits Material 3's DatePicker semantics — day cells expose full-date content descriptions, the mode toggle has its own content description, and navigation controls are labeled — plus ModalBottomSheet's back-press and scrim dismiss handling; the button slots are caller-supplied (typically FormaButtons with M3's 48dp minimum touch target).

---

## DateRangePickerSheet

`dev.formaui.components.datepicker` · tier: extra

A FormaUI date-range picker presented in a modal bottom sheet — Material 3's inline DateRangePicker hosted in a FormaBottomSheet instead of the M3 DatePickerDialog, with hoisted M3 state, confirm/dismiss button slots, and the calendar-to-text-input mode toggle. The picker's scrollable month list is bounded by the sheet so months scroll internally while the buttons stay on-screen.

**`FormaDateRangePickerSheet`** — Hosts M3's stable DateRangePicker in a FormaBottomSheet above an end-aligned button row. State is the caller's DateRangePickerState and the component wires nothing to the button slots — the confirm slot reads state.selectedStartDateMillis / state.selectedEndDateMillis itself (so it can disable itself until a complete range is picked). The default colors make the picker's own container transparent so it sits directly on the sheet surface.

```kotlin
fun FormaDateRangePickerSheet(
    onDismissRequest: () -> Unit,
    state: DateRangePickerState,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: (@Composable () -> Unit)? = null,
    showModeToggle: Boolean = true,
    skipPartiallyExpanded: Boolean = true,
    shape: Shape? = null,
    colors: DatePickerColors? = null,
)
```

- `onDismissRequest` — called when the sheet is dismissed (scrim tap, back press, or swipe down) — hide the sheet by no longer composing it. There is no separate cancel callback; treat a dismiss as closed-without-confirming.
- `state` — the hoisted Material 3 state; create it with rememberDateRangePickerState. Read state.selectedStartDateMillis and state.selectedEndDateMillis (UTC epoch millis, Long?) in the confirm slot's click handler. The picker's locale is captured when the state is created (Material 3 behavior).
- `confirmButton` — the primary action slot (required), typically a FormaButton. The component attaches no click handling or label — the slot owns both.
- `modifier` — the Modifier applied to the sheet.
- `dismissButton` — the optional secondary/cancel action slot, shown before confirmButton.
- `showModeToggle` — whether the picker shows Material 3's calendar-to-text-input mode toggle.
- `skipPartiallyExpanded` — whether the sheet skips the half-expanded state and opens fully. Defaults to true — deliberately unlike FormaBottomSheet — because the half-expanded state clips the calendar grid.
- `shape` — the sheet container shape. When null, Material 3's top-rounded default is used.
- `colors` — the DatePickerColors for the picker. When null, Material 3's defaults are used with a transparent picker container so the picker sits directly on the sheet surface (avoiding a container-on-container seam). A supplied value is passed through untouched.

**Supporting API:**
- `rememberDateRangePickerState` (function (Material 3)) — The component defines no state type of its own — callers create and own the picker state via Material 3's rememberDateRangePickerState (initial range, selectableDates, yearRange, initialDisplayMode, saveability) and read selectedStartDateMillis / selectedEndDateMillis from it.

**Variants:** Calendar mode (internally scrolling month list), Text-input mode (initialDisplayMode or the mode toggle), With dismiss button, Mode toggle hidden, Custom colors

**Example:**

```kotlin
val state = rememberDateRangePickerState()
if (open) {
    FormaDateRangePickerSheet(
        onDismissRequest = { open = false },
        state = state,
        confirmButton = {
            FormaButton(
                onClick = {
                    onPicked(state.selectedStartDateMillis, state.selectedEndDateMillis)
                    open = false
                },
                enabled = state.selectedEndDateMillis != null,
            ) { Text("OK") }
        },
    )
}
```

**Accessibility:** Inherits Material 3's DateRangePicker semantics — day cells expose full-date content descriptions with range start/end announcements, the mode toggle has its own content description, and navigation controls are labeled — plus ModalBottomSheet's back-press and scrim dismiss handling; the button slots are caller-supplied (typically FormaButtons with M3's 48dp minimum touch target).

---

## PullToRefresh

`dev.formaui.components.pulltorefresh` · tier: extra

A FormaUI pull-to-refresh container — wraps a scrollable layout and adds a swipe-down-to-refresh gesture, delegating to Material 3's stable PullToRefreshBox with fully hoisted isRefreshing state and a styling-passthrough indicator slot.

**`FormaPullToRefresh`** — Wraps a scrollable BoxScope content with a pull-down-to-refresh gesture; the caller hoists isRefreshing and reacts in onRefresh. The indicator slot defaults to Material 3's themed PullToRefreshDefaults.Indicator aligned to the top-center (the styling passthrough).

```kotlin
fun FormaPullToRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    state: PullToRefreshState = rememberPullToRefreshState(),
    contentAlignment: Alignment = Alignment.TopStart,
    indicator: @Composable BoxScope.() -> Unit = PullToRefreshDefaults.Indicator(...),
    content: @Composable BoxScope.() -> Unit,
)
```

- `isRefreshing` — whether a refresh is currently occurring; drives the indicator's spinning state. Flip it to true when your refresh starts and back to false when it completes.
- `onRefresh` — called when the user's pull crosses the threshold, requesting a refresh.
- `modifier` — the Modifier applied to the container.
- `state` — the Material 3 PullToRefreshState tracking pull distance (defaults to a rememberPullToRefreshState()).
- `contentAlignment` — the Alignment of children inside the container (defaults to Alignment.TopStart).
- `indicator` — the indicator drawn on top of the content while pulling or refreshing (defaults to Material 3's PullToRefreshDefaults.Indicator aligned top-center, themed by FormaTheme). This is the styling passthrough; supply a custom indicator (e.g. PullToRefreshDefaults.LoadingIndicator) to override.
- `content` — the scrollable content of the container, laid out in a BoxScope (typically a LazyColumn or a Column with Modifier.verticalScroll).

**Supporting API:**
- `rememberPullToRefreshState` (function (Material 3)) — The component defines no state type of its own — callers may create and own the pull state via Material 3's rememberPullToRefreshState; when omitted, FormaPullToRefresh remembers one internally.

**Variants:** Idle, Refreshing, Custom indicator

**Example:**

```kotlin
var refreshing by remember { mutableStateOf(false) }
val scope = rememberCoroutineScope()
FormaPullToRefresh(
    isRefreshing = refreshing,
    onRefresh = {
        refreshing = true
        scope.launch { delay(1500); refreshing = false }
    },
) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(items) { item -> FormaListItem(headline = item) }
    }
}
```

**Accessibility:** Inherits Material 3's PullToRefreshBox gesture and indicator behavior; drive isRefreshing from your data-load state so the spinning indicator reflects the actual busy state.

---

## SwipeToDismiss

`dev.formaui.components.swipedismiss` · tier: extra

A FormaUI swipe-to-dismiss row — lets the user swipe a row horizontally to dismiss it (e.g. delete or archive), delegating to Material 3's stable SwipeToDismissBox with hoisted state, a revealed background slot, and per-direction enable flags.

**`FormaSwipeToDismiss`** — Wraps RowScope content with a horizontal swipe-to-dismiss gesture; the caller creates the state via rememberSwipeToDismissBoxState and reacts to a completed swipe via onDismiss (or by reading state.dismissDirection). The backgroundContent slot is revealed behind the content while swiping — vary it per direction using state.

```kotlin
fun FormaSwipeToDismiss(
    state: SwipeToDismissBoxState,
    backgroundContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    enableDismissFromStartToEnd: Boolean = true,
    enableDismissFromEndToStart: Boolean = true,
    gesturesEnabled: Boolean = true,
    onDismiss: (SwipeToDismissBoxValue) -> Unit = {},
    content: @Composable RowScope.() -> Unit,
)
```

- `state` — the Material 3 SwipeToDismissBoxState tracking the swipe; create it with rememberSwipeToDismissBoxState (initialValue, confirmValueChange, positionalThreshold).
- `backgroundContent` — the content stacked behind content and revealed while swiping, laid out in a RowScope; use state (e.g. state.dismissDirection) to vary it per swipe direction.
- `modifier` — the Modifier applied to the row.
- `enableDismissFromStartToEnd` — whether the row can be dismissed by swiping start-to-end (defaults to true).
- `enableDismissFromEndToStart` — whether the row can be dismissed by swiping end-to-start (defaults to true).
- `gesturesEnabled` — whether the swipe gesture is interactive (defaults to true).
- `onDismiss` — called when content is dismissed, with the resulting SwipeToDismissBoxValue direction (defaults to a no-op).
- `content` — the dismissable content, laid out in a RowScope (typically a FormaListItem or a Surface).

**Supporting API:**
- `rememberSwipeToDismissBoxState` (function (Material 3)) — The component defines no state type of its own — callers create and own the swipe state via Material 3's rememberSwipeToDismissBoxState and read dismissDirection / currentValue from it.
- `SwipeToDismissBoxValue` (enum (Material 3)) — The swipe direction reported to onDismiss and held by the state: StartToEnd, EndToStart, Settled.

**Variants:** Both directions, Single direction (enable flags), Gestures disabled

**Example:**

```kotlin
val state = rememberSwipeToDismissBoxState()
FormaSwipeToDismiss(
    state = state,
    onDismiss = { direction -> onRemove(item) },
    backgroundContent = {
        Box(
            Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(horizontal = FormaTheme.spacing.md),
            contentAlignment = Alignment.CenterEnd,
        ) { Text("Delete", color = MaterialTheme.colorScheme.onErrorContainer) }
    },
) {
    FormaListItem(headline = item.title, supporting = item.subtitle)
}
```

**Accessibility:** Inherits Material 3's SwipeToDismissBox gesture handling; because a swipe gesture is not reachable by all users, pair the swipe action with an equivalent explicit control (e.g. a trailing overflow action) so the dismiss action stays operable without gestures.

---

## RangeSlider

`dev.formaui.components.slider` · tier: extra

A FormaUI range slider — lets the user select a sub-range (start and end value) from a continuous or stepped range, delegating to Material 3's RangeSlider. The sibling of FormaSlider for two-thumb selection.

**`FormaRangeSlider`** — A stateless two-thumb slider with the hoisted value as a ClosedFloatingPointRange; the thumbs are bounded by valueRange and cannot cross. Mirrors FormaSlider's API.

```kotlin
fun FormaRangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: SliderColors? = null,
)
```

- `value` — the current selected range; both ends should sit within valueRange.
- `onValueChange` — called continuously with the new range as the user drags either thumb. The caller must hoist and persist the new value for it to appear.
- `modifier` — the Modifier applied to the slider.
- `enabled` — whether the slider is interactive.
- `valueRange` — the inclusive range of selectable values (defaults to 0f..1f).
- `steps` — the number of discrete intermediate stops (0 = continuous).
- `onValueChangeFinished` — called once when the user finishes changing the range.
- `colors` — the slider colors (defaults to the M3 defaults, themed by FormaTheme).

**Variants:** Continuous, Stepped

**Example:**

```kotlin
var range by remember { mutableStateOf(0.2f..0.8f) }
FormaRangeSlider(
    value = range,
    onValueChange = { range = it },
    modifier = Modifier.fillMaxWidth(),
)
```

**Accessibility:** Each thumb exposes a 48dp touch target via Material 3's minimum interactive component size; the two thumbs are bounded by valueRange and cannot cross each other.

---

## TimePickerSheet

`dev.formaui.components.timepicker` · tier: extra

A FormaUI time picker presented in a modal bottom sheet — Material 3's TimePicker (clock dial) and TimeInput (text entry) hosted in a FormaBottomSheet, with hoisted M3 TimePickerState, confirm/dismiss button slots, and a FormaUI-provided dial-to-input mode toggle (M3's TimePicker has none of its own). Mirrors FormaDatePickerSheet's structure.

**`FormaTimePickerSheet`** — Hosts M3's TimePicker/TimeInput in a FormaBottomSheet above a button row: an optional FormaUI mode toggle at the start and end-aligned dismiss/confirm slots. State is the caller's TimePickerState and the component wires nothing to the button slots — the confirm slot reads state.hour / state.minute itself. The default colors make the picker's own container transparent so it sits directly on the sheet surface.

```kotlin
fun FormaTimePickerSheet(
    onDismissRequest: () -> Unit,
    state: TimePickerState,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: (@Composable () -> Unit)? = null,
    showModeToggle: Boolean = true,
    skipPartiallyExpanded: Boolean = true,
    shape: Shape? = null,
    colors: TimePickerColors? = null,
)
```

- `onDismissRequest` — called when the sheet is dismissed (scrim tap, back press, or swipe down) — hide the sheet by no longer composing it. There is no separate cancel callback; treat a dismiss as closed-without-confirming.
- `state` — the hoisted Material 3 state; create it with rememberTimePickerState. Read state.hour (0-23) and state.minute (0-59) in the confirm slot's click handler. The 12h/24h format and locale are captured when the state is created (Material 3 behavior).
- `confirmButton` — the primary action slot (required), typically a FormaButton. The component attaches no click handling or label — the slot owns both.
- `modifier` — the Modifier applied to the sheet.
- `dismissButton` — the optional secondary/cancel action slot, shown before confirmButton.
- `showModeToggle` — whether to show the FormaUI-provided toggle that swaps the picker between the clock dial and text input. When false, only the clock dial is shown. (Material 3's TimePicker has no built-in toggle, unlike DatePicker.)
- `skipPartiallyExpanded` — whether the sheet skips the half-expanded state and opens fully. Defaults to true — deliberately unlike FormaBottomSheet — because the half-expanded state clips the clock dial.
- `shape` — the sheet container shape. When null, Material 3's top-rounded default is used.
- `colors` — the TimePickerColors applied to both the dial and text-input modes. When null, Material 3's defaults are used with a transparent picker container so the picker sits directly on the sheet surface (avoiding a container-on-container seam). A supplied value is passed through untouched.

**Supporting API:**
- `rememberTimePickerState` (function (Material 3)) — The component defines no state type of its own — callers create and own the picker state via Material 3's rememberTimePickerState (initialHour, initialMinute, is24Hour) and read hour / minute from it.

**Variants:** Clock dial mode, Text-input mode (via the toggle), With dismiss button, Mode toggle hidden, Custom colors

**Example:**

```kotlin
val state = rememberTimePickerState(initialHour = 9, initialMinute = 30)
if (open) {
    FormaTimePickerSheet(
        onDismissRequest = { open = false },
        state = state,
        confirmButton = {
            FormaButton(onClick = { onPicked(state.hour, state.minute); open = false }) { Text("OK") }
        },
    )
}
```

**Accessibility:** Inherits Material 3's TimePicker/TimeInput semantics — the dial and the hour/minute selectors are labeled and the AM/PM toggle is announced — plus ModalBottomSheet's back-press and scrim dismiss handling; the FormaUI mode toggle carries a mode-dependent contentDescription ('Switch to text input' / 'Switch to clock dial'). The button slots are caller-supplied (typically FormaButtons with M3's 48dp minimum touch target).

---

## Carousel

`dev.formaui.components.carousel` · tier: extra

A FormaUI carousel — a horizontally scrolling, snapping strip of items — one entry point covering both Material 3 horizontal carousel families (multi-browse and uncontained) via a variant enum, with hoisted M3 CarouselState and an indexed item slot.

**`FormaCarousel`** — One entry point for both M3 horizontal carousels; a single itemWidth maps to preferredItemWidth (MultiBrowse) or the exact itemWidth (Uncontained), with an opinionated default item spacing and per-variant default fling.

```kotlin
fun FormaCarousel(
    state: CarouselState,
    itemWidth: Dp,
    modifier: Modifier = Modifier,
    variant: FormaCarouselVariant = FormaCarouselVariant.MultiBrowse,
    itemSpacing: Dp = FormaCarouselDefaults.itemSpacing,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    flingBehavior: TargetedFlingBehavior? = null,
    userScrollEnabled: Boolean = true,
    content: @Composable CarouselItemScope.(index: Int) -> Unit,
)
```

- `state` — the hoisted CarouselState; create it with Material 3's rememberCarouselState(itemCount = { ... }).
- `itemWidth` — the item width — the preferred (flexing) width for FormaCarouselVariant.MultiBrowse, the exact width for FormaCarouselVariant.Uncontained.
- `modifier` — the Modifier applied to the carousel (typically sets its height).
- `variant` — the layout strategy (defaults to FormaCarouselVariant.MultiBrowse).
- `itemSpacing` — the gap between items (defaults to FormaCarouselDefaults.itemSpacing, an opinionated small gap; pass 0.dp for Material 3's flush default).
- `contentPadding` — padding around the whole content strip (defaults to none).
- `flingBehavior` — the post-scroll fling behavior. When null, the Material 3 default for the variant is used (a single-advance snap for MultiBrowse, a no-snap fling for Uncontained).
- `userScrollEnabled` — whether the user can scroll the carousel by gesture.
- `content` — the item slot, invoked per item with its index, in a CarouselItemScope (use its mask modifiers, e.g. Modifier.maskClip, to shape items).

**Supporting API:**
- `FormaCarouselVariant` (enum) — Layout strategy of a FormaCarousel: MultiBrowse (default; flexing large/medium/small items), Uncontained (every item keeps the exact width).
- `FormaCarouselDefaults` (object) — Defaults for FormaCarousel: itemSpacing (FormaTheme.spacing.xs — a small opinionated gap, vs Material 3's 0.dp).
- `rememberCarouselState` (function (Material 3)) — The component defines no state type of its own — callers create and own the state via Material 3's rememberCarouselState(initialItem, itemCount).

**Variants:** MultiBrowse, Uncontained

**Example:**

```kotlin
val state = rememberCarouselState(itemCount = { photos.size })
FormaCarousel(
    state = state,
    itemWidth = 220.dp,
    modifier = Modifier.height(200.dp),
) { index ->
    Image(
        painter = photos[index].painter,
        contentDescription = photos[index].description,
        modifier = Modifier.fillMaxSize().maskClip(FormaTheme.shapes.lg),
        contentScale = ContentScale.Crop,
    )
}
```

**Accessibility:** The carousel is a horizontally scrollable container; supply a meaningful contentDescription on each item's own content (e.g. the Image inside the slot). Material 3 handles the scroll/snap semantics.

---

## ExposedDropdownMenu

`dev.formaui.components.autocomplete` · tier: extra

A FormaUI exposed dropdown menu (autocomplete) — a FormaTextField anchor that drops a menu of options below it, delegating to Material 3's ExposedDropdownMenuBox. Fully hoisted (query text, expanded state, and the already-filtered options are the caller's); FormaUI provides the wiring, the rotating chevron, and the option rows. Generic over the option type via optionLabel.

**`FormaExposedDropdownMenu`** — Generic <T> autocomplete: caller owns value/onValueChange, expanded/onExpandedChange, and the pre-filtered options; editable=true is a type-to-filter field (PrimaryEditable), editable=false is a read-only tap-to-select field (PrimaryNotEditable). Empty options while expanded shows a disabled 'No matches' row.

```kotlin
fun FormaExposedDropdownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    options: List<T>,
    onOptionSelected: (T) -> Unit,
    optionLabel: (T) -> String,
    modifier: Modifier = Modifier,
    variant: FormaTextFieldVariant = FormaTextFieldVariant.Outlined,
    editable: Boolean = true,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    helperText: String? = null,
    errorText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors? = null,
    noMatchesText: String = FormaExposedDropdownMenuDefaults.NoMatchesText,
)
```

- `expanded` — whether the menu is currently open.
- `onExpandedChange` — called when the menu requests to open or close (anchor tap, dismiss).
- `value` — the current query / selected text shown in the anchor field.
- `onValueChange` — called as the user edits the query. Only fires while editable is true.
- `options` — the options to show, already filtered by the caller for the current value.
- `onOptionSelected` — called with the chosen option when a row is tapped; the menu then collapses. The caller reflects the choice into value (e.g. value = optionLabel(it)).
- `optionLabel` — maps an option to its display (and menu-row) text.
- `modifier` — the Modifier applied to the ExposedDropdownMenuBox.
- `variant` — the anchor field's container style (defaults to FormaTextFieldVariant.Outlined).
- `editable` — whether the field is a type-to-filter autocomplete (true) or a read-only tap-to-select field (false).
- `enabled` — whether the control is interactive (disables both the field and the menu anchor).
- `label` — optional floating label on the anchor field.
- `placeholder` — optional placeholder shown while the field is empty.
- `isError` — whether the anchor field is in the error state.
- `helperText` — optional supporting text shown below the field when not in error.
- `errorText` — optional supporting text shown below the field while isError is true.
- `leadingIcon` — optional slot at the start of the anchor field.
- `colors` — the anchor field colors. When null, the M3 defaults for the variant are used.
- `noMatchesText` — the label of the single disabled row shown when options is empty while expanded (defaults to FormaExposedDropdownMenuDefaults.NoMatchesText, "No matches").

**Supporting API:**
- `FormaExposedDropdownMenuDefaults` (object) — Defaults for FormaExposedDropdownMenu: NoMatchesText ("No matches") — the label of the disabled empty-options row.

**Variants:** Editable (type-to-filter), Non-editable (tap-to-select), Empty (No matches)

**Example:**

```kotlin
var text by remember { mutableStateOf("") }
var expanded by remember { mutableStateOf(false) }
val matches = countries.filter { it.contains(text, ignoreCase = true) }
FormaExposedDropdownMenu(
    expanded = expanded,
    onExpandedChange = { expanded = it },
    value = text,
    onValueChange = { text = it },
    options = matches,
    onOptionSelected = { text = it },
    optionLabel = { it },
    label = "Country",
)
```

**Accessibility:** The rotating chevron trailing icon and the anchor's expand/collapse + option semantics are handled by Material 3's ExposedDropdownMenuBox / menuAnchor; the trailing icon is the standard M3 ExposedDropdownMenuDefaults.TrailingIcon. The field's error state is surfaced to accessibility services by the underlying FormaTextField.

---
