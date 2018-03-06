package org.codice.ddf.admin.query.configuration.fields;

import java.util.List;
import java.util.concurrent.Callable;

import org.codice.ddf.admin.api.Field;
import org.codice.ddf.admin.query.configuration.services.api.InstallationProfile;
import org.codice.ddf.admin.common.fields.base.BaseListField;
import org.codice.ddf.admin.common.fields.base.BaseObjectField;
import org.codice.ddf.admin.common.fields.base.scalar.StringField;

public class InstallationProfileField extends BaseObjectField {

  public static final String DEFAULT_FIELD_NAME = "installationProfile";
  public static final String FIELD_TYPE_NAME = "InstallationProfile";
  public static final String DESCRIPTION = "Describes a preset of configurations to be installed in the system.";

  private static final String PROFILE_ID = "id";
  private static final String PROFILE_NAME = "name";
  private static final String PROFILE_DESCRIPTION = "description";

  private StringField profileId;
  private StringField profileName;
  private StringField profileDescription;

  // TODO: tbatie - 3/2/18 - Add different configurations

  public InstallationProfileField() {
    super(DEFAULT_FIELD_NAME, FIELD_TYPE_NAME, DESCRIPTION);
    profileId = new StringField(PROFILE_ID);
    profileName = new StringField(PROFILE_NAME);
    profileDescription = new StringField(PROFILE_DESCRIPTION);
  }

  public InstallationProfileField(InstallationProfile installationProfile) {
    this();
    profileId.setValue(installationProfile.getId());
    profileName.setValue(installationProfile.getName());
    profileDescription.setValue(installationProfile.getDescription());

  }
  @Override
  public List<Field> getFields() {
    return null;
  }

  public static class ListImpl extends BaseListField<InstallationProfileField> {

    public static final String DEFAULT_FIELD_NAME = "profiles";

    public ListImpl() {
      super(DEFAULT_FIELD_NAME);
    }

    public ListImpl(List<InstallationProfile> profiles) {
      this();
      profiles.stream().map(InstallationProfileField::new).forEach(this::add);
    }

    @Override
    public Callable<InstallationProfileField> getCreateListEntryCallable() {
      return InstallationProfileField::new;
    }
  }
}
