package libui                                                                  

import kotlinx.cinterop.*

///////////////////////////////////////////////////////////////////////////////
//
// Container widgets:
// - [Window]
// - [Form]
// - [Grid]
// - [Box]
// - [Tab]
// - [Group]
//
// Data entry widgets:
// - [Entry]
// - [MultilineEntry]
// - [Checkbox]
// - [Combobox]
// - [EditableCombobox]
// - [Spinbox]
// - [Slider]
// - [RadioButtons]
// - [DateTimePicker]
//
// Static widgets:
// - [Label]
// - [Separator]
// - [ProgressBar]
//
// Buttons:
// - [Button]
// - [ColorButton]
// - [FontButton]
//
///////////////////////////////////////////////////////////////////////////////

/** Represents a top-level window.
 *  Contains one child control that occupies the entirety of the window. */
typealias Window = CPointer<uiWindow>

/** Create a new Window. */
fun Window(title: String, width: Int, height: Int, hasMenubar: Boolean = true) : Window
    = uiNewWindow(title, width, height, if (hasMenubar) 1 else 0) ?: throw Error()

/** Destroy and free the Window. */
fun Window.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this Window.
 *  - On Windows this is an HWND of a libui-internal class.
 *  - On GTK+ this is a pointer to a GtkWindow.
 *  - On macOS this is a pointer to a NSWindow. */
val Window.handle: Long get() = uiControlHandle(reinterpret())

/** Set or return the text to show in window title bar. */
var Window.title: String
    get() = uiWindowTitle(this)?.toKString() ?: ""
    set(title) = uiWindowSetTitle(this, title)

/** Allow to specify that the window is a frameless one, without borders,
    title bar and OS window control widgets. */
var Window.borderless: Boolean
    get() = uiWindowBorderless(this) != 0
    set(borderless) = uiWindowSetBorderless(this, if (borderless) 1 else 0)

/** Specify if the Window content should have a margin or not. Defaults to `false`. */
var Window.margined: Boolean
    get() = uiWindowMargined(this) != 0
    set(margined) = uiWindowSetMargined(this, if (margined) 1 else 0)

/** Whether the window should show in fullscreen or not. */
var Window.fullscreen: Boolean
    get() = uiWindowFullscreen(this) != 0
    set(fullscreen) = uiWindowSetFullscreen(this, if (fullscreen) 1 else 0)

//void uiWindowContentSize(uiWindow *w, int *width, int *height)
//void uiWindowSetContentSize(uiWindow *w, int width, int height)

//void uiWindowSetChild(uiWindow *w, uiControl *child)

/** Show the window. */
fun Window.show() = uiControlShow(reinterpret())

//void uiWindowOnContentSizeChanged(uiWindow *w, void (*f)(uiWindow *, void *), void *data)

fun Window.onClosing(proc: Window.() -> Int)
    = uiWindowOnClosing(this, staticCFunction(::_onClosing), StableRef.create(proc).asCPointer())

internal fun _onClosing(window: Window?, data: COpaquePointer?): Int {
    val ref = data!!.asStableRef<Window.() -> Int>()
    val proc = ref.get()
    ref.dispose()
    return window!!.proc()
}

///////////////////////////////////////////////////////////////////////////////

/** A container that organize children as labeled fields. */
typealias Form = CPointer<uiForm>

/** Create a new Form. */
fun Form() : Form = uiNewForm() ?: throw Error()

/** Destroy and free the Form. */
fun Form.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this Form. */
val Form.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the Form should be enabled or disabled. Defaults to `true`. */
var Form.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the Form should be visible or hidden. Defaults to `true`. */
var Form.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

/** If true, the container insert some space between children. */
var Form.padded: Boolean
    get() = uiFormPadded(this) != 0
    set(padded) = uiFormSetPadded(this, if (padded) 1 else 0)

//void uiFormAppend(uiForm *f, const char *label, uiControl *c, int stretchy)
//void uiFormDelete(uiForm *f, int index)

///////////////////////////////////////////////////////////////////////////////

/** A powerful container that allow to specify size and position of each children. */
typealias Grid = CPointer<uiGrid>

/** Create a new Grid. */
fun Grid() : Grid = uiNewGrid() ?: throw Error()

/** Destroy and free the Grid. */
fun Grid.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this Grid. */
val Grid.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the Grid should be enabled or disabled. Defaults to `true`. */
var Grid.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the Grid should be visible or hidden. Defaults to `true`. */
var Grid.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

/** If true, the container insert some space between children. */
var Grid.padded: Boolean
    get() = uiGridPadded(this) != 0
    set(padded) = uiGridSetPadded(this, if (padded) 1 else 0)

//void uiGridAppend(uiGrid *g, uiControl *c, int left, int top, int xspan, int yspan, int hexpand, uiAlign halign, int vexpand, uiAlign valign)
//void uiGridInsertAt(uiGrid *g, uiControl *c, uiControl *existing, uiAt at, int xspan, int yspan, int hexpand, uiAlign halign, int vexpand, uiAlign valign)

///////////////////////////////////////////////////////////////////////////////

/** A container that stack its chidren horizontally or vertically. */
typealias Box = CPointer<uiBox>

/** Create a new Box object that stack its chidren horizontally. */
fun HorizontalBox() : Box = uiNewHorizontalBox() ?: throw Error()

/** Create a new Box object that stack its chidren vertically. */
fun VerticalBox() : Box = uiNewVerticalBox() ?: throw Error()

/** Destroy and free the Box. */
fun Box.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this Box.
 *  - On Windows this is an HWND of a libui-internal class.
 *  - On GTK+ this is a pointer to a GtkBox.
 *  - On OS X this is a pointer to a NSView. */
val Box.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the Box should be enabled or disabled. Defaults to `true`. */
var Box.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the Box should be visible or hidden. Defaults to `true`. */
var Box.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

/** If true, the container insert some space between children. Defaults to false. */
var Box.padded: Boolean
    get() = uiBoxPadded(this) != 0
    set(padded) = uiBoxSetPadded(this, if (padded) 1 else 0)

//void uiBoxAppend(uiBox *b, uiControl *child, int stretchy)
//void uiBoxDelete(uiBox *b, int index)

///////////////////////////////////////////////////////////////////////////////

/** A container that show each chidren in a separate tab. */
typealias Tab = CPointer<uiTab>

/** Create a new Tab. */
fun Tab() : Tab = uiNewTab() ?: throw Error()

/** Destroy and free the Tab. */
fun Tab.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this Tab.
 *  - On Windows this is an HWND of a standard Windows API WC_TABCONTROL class
 *    (as provided by Common Controls version 6).
 *    The pages are not children of this window and there currently
 *    is no way to directly access them.
 *  - On GTK+ this is a pointer to a GtkNotebook.
 *  - On OS X this is a pointer to a NSTabView. */
val Tab.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the Tab should be enabled or disabled. Defaults to `true`. */
var Tab.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the Tab should be visible or hidden. Defaults to `true`. */
var Tab.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

//int uiTabMargined(uiTab *t, int page)
//void uiTabSetMargined(uiTab *t, int page, int margined)
//void uiTabAppend(uiTab *t, const char *name, uiControl *c)
//void uiTabInsertAt(uiTab *t, const char *name, int before, uiControl *c)
//void uiTabDelete(uiTab *t, int index)
//int uiTabNumPages(uiTab *t)

///////////////////////////////////////////////////////////////////////////////

/** A container for a single widget that provide a caption and visually group it's children. */
typealias Group = CPointer<uiGroup>

/** Create a new Group. */
fun Group(text: String) : Group = uiNewGroup(text) ?: throw Error()

/** Destroy and free the Group. */
fun Group.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this Group.
 *  - On Windows this is an HWND of a standard Windows API BUTTON class
 *    (as provided by Common Controls version 6).
 *  - On GTK+ this is a pointer to a GtkFrame.
 *  - On OS X this is a pointer to a NSBox. */
val Group.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the Group should be enabled or disabled. Defaults to `true`. */
var Group.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the Group should be visible or hidden. Defaults to `true`. */
var Group.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

/** Specify the caption of the group. */
var Group.title: String
    get() = uiGroupTitle(this)?.toKString() ?: ""
    set(title) = uiGroupSetTitle(this, title)

/** Specify if the group content area should have a margin or not. */
var Group.margined: Boolean
    get() = uiGroupMargined(this) != 0
    set(margined) = uiGroupSetMargined(this, if (margined) 1 else 0)

//void uiGroupSetChild(uiGroup *g, uiControl *c)

///////////////////////////////////////////////////////////////////////////////

/** A simple, single line text entry widget. */
typealias Entry = CPointer<uiEntry>

/** Create a new simple text Entry. */
fun Entry() : Entry = uiNewEntry() ?: throw Error()

/** Create a new text Entry widget that mask the input,
    useful to edit passwords or other sensible data. */
fun PasswordEntry() : Entry = uiNewPasswordEntry() ?: throw Error()

/** Create a new text Entry to search text. */
fun SearchEntry() : Entry = uiNewSearchEntry() ?: throw Error()

/** Destroy and free the Entry. */
fun Entry.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this Entry.
 *  - On Windows this is an HWND of a standard Windows API EDIT class
 *    (as provided by Common Controls version 6).
 *  - On GTK+ this is a pointer to a GtkEntry.
 *  - On OS X this is a pointer to a NSTextField. */
val Entry.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the Entry should be enabled or disabled. Defaults to `true`. */
var Entry.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the Entry should be visible or hidden. Defaults to `true`. */
var Entry.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

/** The current text of the Entry. */
var Entry.text: String
    get() = uiEntryText(this)?.toKString() ?: ""
    set(text) = uiEntrySetText(this, text)

/** Whether the user is allowed to change the entry text. Defaults to `true`. */
var Entry.readonly: Boolean
    get() = uiEntryReadOnly(this) != 0
    set(readonly) = uiEntrySetReadOnly(this, if (readonly) 1 else 0)

//void uiEntryOnChanged(uiEntry *e, void (*f)(uiEntry *e, void *data), void *data)

///////////////////////////////////////////////////////////////////////////////

/** A multiline text entry widget. */
typealias MultilineEntry = CPointer<uiMultilineEntry>

/** Create a new MultilineEntry. */
fun MultilineEntry() : MultilineEntry = uiNewMultilineEntry() ?: throw Error()

/** Create a new non wrapping MultilineEntry. */
fun NonWrappingMultilineEntry() : MultilineEntry = uiNewNonWrappingMultilineEntry() ?: throw Error()

/** Destroy and free the MultilineEntry. */
fun MultilineEntry.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this MultilineEntry. */
val MultilineEntry.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the MultilineEntry should be enabled or disabled. Defaults to `true`. */
var MultilineEntry.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the MultilineEntry should be visible or hidden. Defaults to `true`. */
var MultilineEntry.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

/** The current text of the multiline entry. */
var MultilineEntry.text: String
    get() = uiMultilineEntryText(this)?.toKString() ?: ""
    set(text) = uiMultilineEntrySetText(this, text)

/** Whether the user is allowed to change the entry text. */
var MultilineEntry.readOnly: Boolean
    get() = uiMultilineEntryReadOnly(this) != 0
    set(readOnly) = uiMultilineEntrySetReadOnly(this, if (readOnly) 1 else 0)

//void uiMultilineEntryAppend(uiMultilineEntry *e, const char *text)
//void uiMultilineEntryOnChanged(uiMultilineEntry *e, void (*f)(uiMultilineEntry *e, void *data), void *data)

///////////////////////////////////////////////////////////////////////////////

/** A checkbox widget. */
typealias Checkbox = CPointer<uiCheckbox>

/** Create a new Checkbox. */
fun Checkbox(text: String) : Checkbox = uiNewCheckbox(text) ?: throw Error()

/** Destroy and free the Checkbox. */
fun Checkbox.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this Checkbox.
 *  - On Windows this is an HWND of a standard Windows API BUTTON class
 *    (as provided by Common Controls version 6).
 *  - On GTK+ this is a pointer to a GtkCheckButton.
 *  - On OS X this is a pointer to a NSButton. */
val Checkbox.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the Checkbox should be enabled or disabled. Defaults to `true`. */
var Checkbox.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the Checkbox should be visible or hidden. Defaults to `true`. */
var Checkbox.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

/** The static text of the checkbox. */
var Checkbox.text: String
    get() = uiCheckboxText(this)?.toKString() ?: ""
    set(text) = uiCheckboxSetText(this, text)

/** Whether the checkbox is checked or unchecked. Defaults to `false`. */
var Checkbox.checked: Boolean
    get() = uiCheckboxChecked(this) != 0
    set(checked) = uiCheckboxSetChecked(this, if (checked) 1 else 0)

//void uiCheckboxOnToggled(uiCheckbox *c, void (*f)(uiCheckbox *c, void *data), void *data)

///////////////////////////////////////////////////////////////////////////////

/** A drop down combo box that allow list selection only. */
typealias Combobox = CPointer<uiCombobox>

/** Create a new Combobox. */
fun Combobox() : Combobox = uiNewCombobox() ?: throw Error()

/** Destroy and free the Combobox. */
fun Combobox.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this Combobox.
 *  - On Windows this is an HWND of a standard Windows API COMBOBOX class
 *    (as provided by Common Controls version 6).
 *  - On GTK+ this is a pointer to a GtkComboBoxText.
 *  - On OS X this is a pointer to a NSComboBox for editable Comboboxes
 *    and to a NSPopUpButton for noneditable Comboboxes. */
val Combobox.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the Combobox should be enabled or disabled. Defaults to `true`. */
var Combobox.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the Combobox should be visible or hidden. Defaults to `true`. */
var Combobox.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

//int uiComboboxSelected(uiCombobox *c)
//void uiComboboxSetSelected(uiCombobox *c, int n)

//void uiComboboxAppend(uiCombobox *c, const char *text)
//void uiComboboxOnSelected(uiCombobox *c, void (*f)(uiCombobox *c, void *data), void *data)

///////////////////////////////////////////////////////////////////////////////

/** A drop down combo box that allow selection from list or free text entry. */
typealias EditableCombobox = CPointer<uiEditableCombobox>

/** Create a new EditableCombobox. */
fun EditableCombobox() : EditableCombobox = uiNewEditableCombobox() ?: throw Error()

/** Return or set the current selected text or the text value of the selected item in the list. */
var EditableCombobox.text: String
    get() = uiEditableComboboxText(this)?.toKString() ?: ""
    set(text) = uiEditableComboboxSetText(this, text)

//void uiEditableComboboxAppend(uiEditableCombobox *c, const char *text)
//// TODO what do we call a function that sets the currently selected item and fills the text field with it? editable comboboxes have no consistent concept of selected item
//void uiEditableComboboxOnChanged(uiEditableCombobox *c, void (*f)(uiEditableCombobox *c, void *data), void *data)

///////////////////////////////////////////////////////////////////////////////

//// spinbox/slider rules:
//// setting value outside of range will automatically clamp
//// initial value is minimum
//// complaint if min >= max?

/** An entry widget for numerical values. */
typealias Spinbox = CPointer<uiSpinbox>

/** Create a new Spinbox. */
fun Spinbox(min: Int, max: Int) : Spinbox = uiNewSpinbox(min, max) ?: throw Error()

/** Destroy and free the Spinbox. */
fun Spinbox.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this Spinbox.
 *  - On Windows this is an HWND of a standard Windows API EDIT class
 *    (as provided by Common Controls version 6).
 *    Due to various limitations which affect the lifetime of the associated
 *    Common Controls version 6 UPDOWN_CLASS window that provides the buttons,
 *    there is no way to access it.
 *  - On GTK+ this is a pointer to a GtkSpinButton.
 *  - On OS X this is a pointer to a NSView that contains a NSTextField
 *    and a NSStepper as subviews. */
val Spinbox.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the Spinbox should be enabled or disabled. Defaults to `true`. */
var Spinbox.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the Spinbox should be visible or hidden. Defaults to `true`. */
var Spinbox.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

/** The current numeric value of the spinbox. */
var Spinbox.value: Int
    get() = uiSpinboxValue(this)
    set(value) = uiSpinboxSetValue(this, value)

//void uiSpinboxOnChanged(uiSpinbox *s, void (*f)(uiSpinbox *s, void *data), void *data)

///////////////////////////////////////////////////////////////////////////////

/** Horizontal slide to set numerical values. */
typealias Slider = CPointer<uiSlider>

/** Create a new Slider. */
fun Slider(min: Int, max: Int) : Slider = uiNewSlider(min, max) ?: throw Error()

/** Destroy and free the Slider. */
fun Slider.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this Slider.
 *  - On Windows this is an HWND of a standard Windows API TRACKBAR_CLASS class
 *    (as provided by Common Controls version 6).
 *  - On GTK+ this is a pointer to a GtkScale.
 *  - On OS X this is a pointer to a NSSlider. */
val Slider.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the Slider should be enabled or disabled. Defaults to `true`. */
var Slider.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the Slider should be visible or hidden. Defaults to `true`. */
var Slider.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

/** The current numeric value of the slider. */
var Slider.value: Int
    get() = uiSliderValue(this)
    set(value) = uiSliderSetValue(this, value)

//void uiSliderOnChanged(uiSlider *s, void (*f)(uiSlider *s, void *data), void *data)

///////////////////////////////////////////////////////////////////////////////

/** A widget that represent a group of radio options. */
typealias RadioButtons = CPointer<uiRadioButtons>

/** Create a new RadioButtons. */
fun RadioButtons() : RadioButtons = uiNewRadioButtons() ?: throw Error()

/** Destroy and free the RadioButtons. */
fun RadioButtons.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this RadioButtons.
 *  - On Windows this is an HWND of a libui-internal class;
 *    its child windows are instances of the standard Windows API BUTTON class
 *    (as provided by Common Controls version 6).
 *  - On GTK+ this is a pointer to a GtkBox containing GtkRadioButtons.
 *  - On OS X this is a pointer to a NSView with each radio button as a NSButton subview. */
val RadioButtons.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the RadioButtons should be enabled or disabled. Defaults to `true`. */
var RadioButtons.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the RadioButtons should be visible or hidden. Defaults to `true`. */
var RadioButtons.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

/** Return or set the current choosed option by index. */
var RadioButtons.selected: Int
    get() = uiRadioButtonsSelected(this)
    set(value) = uiRadioButtonsSetSelected(this, value)

//void uiRadioButtonsAppend(uiRadioButtons *r, const char *text)
//void uiRadioButtonsOnSelected(uiRadioButtons *r, void (*f)(uiRadioButtons *, void *), void *data)

///////////////////////////////////////////////////////////////////////////////

/** A widgets to edit date/times. */
typealias DateTimePicker = CPointer<uiDateTimePicker>

/** Create a new DateTimePicker to edit date/times. */
fun DateTimePicker() : DateTimePicker = uiNewDateTimePicker() ?: throw Error()

/** Create a new DateTimePicker to edit dates. */
fun DatePicker() : DateTimePicker = uiNewDatePicker() ?: throw Error()

/** Create a new DateTimePicker to edit times. */
fun TimePicker() : DateTimePicker = uiNewTimePicker() ?: throw Error()

/** Destroy and free the DateTimePicker. */
fun DateTimePicker.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this DateTimePicker.
 *  - On Windows this is an HWND of a standard Windows API DATETIMEPICK_CLASS class
 *    (as provided by Common Controls version 6).
 *  - On GTK+ this is a pointer to a libui-internal class.
 *  - On OS X this is a pointer to a NSDatePicker. */
val DateTimePicker.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the DateTimePicker should be enabled or disabled. Defaults to `true`. */
var DateTimePicker.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the DateTimePicker should be visible or hidden. Defaults to `true`. */
var DateTimePicker.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

//struct tm
//// TODO document that tm_wday and tm_yday are undefined, and tm_isdst should be -1
//void uiDateTimePickerTime(uiDateTimePicker *d, struct tm *time)
//void uiDateTimePickerSetTime(uiDateTimePicker *d, const struct tm *time)
//void uiDateTimePickerOnChanged(uiDateTimePicker *d, void (*f)(uiDateTimePicker *, void *), void *data)

///////////////////////////////////////////////////////////////////////////////

/** A static text label. */
typealias Label = CPointer<uiLabel>

/** Create a new Label. */
fun Label(text: String) : Label = uiNewLabel(text) ?: throw Error()

/** Destroy and free the Label. */
fun Label.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this Label.
 *  - On Windows this is an HWND of a standard Windows API STATIC class
 *    (as provided by Common Controls version 6).
 *  - On GTK+ this is a pointer to a GtkLabel.
 *  - On OS X this is a pointer to a NSTextField. */
val Label.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the Label should be enabled or disabled. Defaults to `true`. */
var Label.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the Label should be visible or hidden. Defaults to `true`. */
var Label.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

/** The static text of the label. */
var Label.text: String
    get() = uiLabelText(this)?.toKString() ?: ""
    set(text) = uiLabelSetText(this, text)

///////////////////////////////////////////////////////////////////////////////

/** A vertical or an horizontal line to visually separate widgets. */
typealias Separator = CPointer<uiSeparator>

/** Create a new Separator object - an horizontal line to visually separate widgets. */
fun HorizontalSeparator() : Separator = uiNewHorizontalSeparator() ?: throw Error()

/** Create a new Separator object - a vertical line to visually separate widgets. */
fun VerticalSeparator() : Separator = uiNewVerticalSeparator() ?: throw Error()

/** Destroy and free the Separator. */
fun Separator.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this Separator.
 *  - On Windows this is an HWND of a standard Windows API STATIC class
 *    (as provided by Common Controls version 6).
 *  - On GTK+ this is a pointer to a GtkSeparator.
 *  - On OS X this is a pointer to a NSBox. */
val Separator.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the Separator should be enabled or disabled. Defaults to `true`. */
var Separator.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the Separator should be visible or hidden. Defaults to `true`. */
var Separator.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

///////////////////////////////////////////////////////////////////////////////

/** Progress bar widget. */
typealias ProgressBar = CPointer<uiProgressBar>

/** Create a new ProgressBar. */
fun ProgressBar() : ProgressBar = uiNewProgressBar() ?: throw Error()

/** Destroy and free the ProgressBar. */
fun ProgressBar.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this ProgressBar.
 *  - On Windows this is an HWND of a standard Windows API PROGRESS_CLASS class
 *    (as provided by Common Controls version 6).
 *  - On GTK+ this is a pointer to a GtkProgressBar.
 *  - On OS X this is a pointer to a NSProgressIndicator. */
val ProgressBar.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the ProgressBar should be enabled or disabled. Defaults to `true`. */
var ProgressBar.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the ProgressBar should be visible or hidden. Defaults to `true`. */
var ProgressBar.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

/** The current position of the progress bar.
    Could be setted to -1 to create an indeterminate progress bar. */
var ProgressBar.value: Int
    get() = uiProgressBarValue(this)
    set(value) = uiProgressBarSetValue(this, value)

///////////////////////////////////////////////////////////////////////////////

/** A simple button. */
typealias Button = CPointer<uiButton>

/** Create a new Button. */
fun Button(text: String) : Button = uiNewButton(text) ?: throw Error()

/** Destroy and free the Button. */
fun Button.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this Button.
 *  - On Windows this is an HWND of a standard Windows API BUTTON class
 *    (as provided by Common Controls version 6).
 *  - On GTK+ this is a pointer to a GtkButton.
 *  - On OS X this is a pointer to a NSButton. */
val Button.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the Button should be enabled or disabled. Defaults to `true`. */
var Button.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the Button should be visible or hidden. Defaults to `true`. */
var Button.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

/** The static text of the Button. */
var Button.text: String
    get() = uiButtonText(this)?.toKString() ?: ""
    set(text) = uiButtonSetText(this, text)

//void uiButtonOnClicked(uiButton *b, void (*f)(uiButton *b, void *data), void *data)

///////////////////////////////////////////////////////////////////////////////

/** A button that opens a color palette popup. */
typealias ColorButton = CPointer<uiColorButton>

/** Create a new ColorButton. */
fun ColorButton() : ColorButton = uiNewColorButton() ?: throw Error()

/** Destroy and free the ColorButton. */
fun ColorButton.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this ColorButton. */
val ColorButton.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the ColorButton should be enabled or disabled. Defaults to `true`. */
var ColorButton.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the ColorButton should be visible or hidden. Defaults to `true`. */
var ColorButton.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

//void uiColorButtonColor(uiColorButton *b, double *r, double *g, double *bl, double *a)
//void uiColorButtonSetColor(uiColorButton *b, double r, double g, double bl, double a)
//void uiColorButtonOnChanged(uiColorButton *b, void (*f)(uiColorButton *, void *), void *data)

///////////////////////////////////////////////////////////////////////////////

/** A button that allows users to choose a font when they click on it. */
typealias FontButton = CPointer<uiFontButton>

/** Creates a new FontButton. The default font is OS-defined. */
fun FontButton() : FontButton = uiNewFontButton() ?: throw Error()

/** Destroy and free the FontButton. */
fun FontButton.destroy() = uiControlDestroy(reinterpret())

/** Returns the OS-level handle associated with this FontButton. */
val FontButton.handle: Long get() = uiControlHandle(reinterpret())

/** Whether the FontButton should be enabled or disabled. Defaults to `true`. */
var FontButton.enabled: Boolean
    get() = uiControlEnabled(reinterpret()) != 0
    set(enabled) = if (enabled) uiControlEnable(reinterpret()) else uiControlDisable(reinterpret())

/** Whether the FontButton should be visible or hidden. Defaults to `true`. */
var FontButton.visible: Boolean
    get() = uiControlVisible(reinterpret()) != 0
    set(visible) = if (visible) uiControlShow(reinterpret()) else uiControlHide(reinterpret())

//// uiFontButtonFont() returns the font currently selected in the uiFontButton in desc.
//// uiFontButtonFont() allocates resources in desc when you are done with the font, call uiFreeFontButtonFont() to release them.
//// uiFontButtonFont() does not allocate desc itself you must do so.
//// TODO have a function that sets an entire font descriptor to a range in a uiAttributedString at once, for SetFont?
//void uiFontButtonFont(uiFontButton *b, uiFontDescriptor *desc)

//// TOOD SetFont, mechanics
//// uiFontButtonOnChanged() sets the function that is called when the font in the uiFontButton is changed.
//void uiFontButtonOnChanged(uiFontButton *b, void (*f)(uiFontButton *, void *), void *data)

//// uiFreeFontButtonFont() frees resources allocated in desc by uiFontButtonFont().
//// After calling uiFreeFontButtonFont(), the contents of desc should be assumed to be undefined (though since you allocate desc itself, you can safely reuse desc for other font descriptors).
//// Calling uiFreeFontButtonFont() on a uiFontDescriptor not returned by uiFontButtonFont() results in undefined behavior.
//void uiFreeFontButtonFont(uiFontDescriptor *desc)