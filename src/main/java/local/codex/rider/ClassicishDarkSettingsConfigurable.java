package local.codex.rider;

import com.intellij.openapi.options.SearchableConfigurable;

import javax.swing.JComponent;

public final class ClassicishDarkSettingsConfigurable implements SearchableConfigurable {
  private ClassicishDarkSettingsComponent component;

  @Override
  public String getId() {
    return "io.github.zip83.rider.classicish.dark.ui.settings";
  }

  @Override
  public String getDisplayName() {
    return "Rider Classic-ish Dark UI";
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return component.getPreferredFocusedComponent();
  }

  @Override
  public JComponent createComponent() {
    component = new ClassicishDarkSettingsComponent();
    return component.getPanel();
  }

  @Override
  public boolean isModified() {
    ClassicishDarkSettings settings = ClassicishDarkSettings.getInstance();
    return component.isExtendedToolWindowsUiReminderEnabled()
      != settings.isExtendedToolWindowsUiReminderEnabled()
      || !component.createProfileOptions().equals(StartupProfileOptions.fromSettings(settings));
  }

  @Override
  public void apply() {
    ClassicishDarkSettings settings = ClassicishDarkSettings.getInstance();
    StartupProfileOptions previousOptions = StartupProfileOptions.fromSettings(settings);
    StartupProfileOptions options = component.createProfileOptions();

    StartupProfile.create(previousOptions, options).apply();
    settings.setExtendedToolWindowsUiReminderEnabled(component.isExtendedToolWindowsUiReminderEnabled());
    settings.setProfileOptions(options);
  }

  @Override
  public void reset() {
    ClassicishDarkSettings settings = ClassicishDarkSettings.getInstance();
    component.setExtendedToolWindowsUiReminderEnabled(settings.isExtendedToolWindowsUiReminderEnabled());
    component.setProfileOptions(StartupProfileOptions.fromSettings(settings));
  }

  @Override
  public void disposeUIResources() {
    component = null;
  }
}
