package org.codice.ddf.admin.fields;

import java.util.List;
import java.util.concurrent.Callable;

import org.codice.ddf.admin.api.Field;
import org.codice.ddf.admin.services.api.CapabilitiesProfile;
import org.codice.ddf.admin.common.fields.base.BaseListField;
import org.codice.ddf.admin.common.fields.base.BaseObjectField;
import org.codice.ddf.admin.common.fields.base.scalar.StringField;

import com.google.common.collect.ImmutableList;

public class CapabilitiesProfileField extends BaseObjectField {

  private static final String DEFAULT_FIELD_NAME = "profile";
  private static final String FIELD_TYPE_NAME = "Profile";
  private static final String DESCRIPTION = "Represents a meaningful set of capabilities";

  private static final String PROFILE_NAME = "name";
  private static final String PROFILE_DESCRIPTION = "description";

  private StringField id;
  private StringField profileName;
  private StringField profileDescription;
  private CapabilityField.ListImpl capabilities;

  public CapabilitiesProfileField() {
    super(DEFAULT_FIELD_NAME, FIELD_TYPE_NAME, DESCRIPTION);
    id = new StringField("id");
    profileName = new StringField(PROFILE_NAME);
    profileDescription = new StringField(PROFILE_DESCRIPTION);
    capabilities = new CapabilityField.ListImpl();
  }

  public CapabilitiesProfileField(CapabilitiesProfile profile) {
    this();
    id.setValue(profile.getId());
    profileName.setValue(profile.getName());
    profileDescription.setValue(profile.getDescription());
    capabilities = new CapabilityField.ListImpl(profile.getCapabilities());
  }

  @Override
  public List<Field> getFields() {
    return ImmutableList.of(profileName, profileDescription, capabilities);
  }

  public static class ListImpl extends BaseListField<CapabilitiesProfileField> {

    public static final String DEFAULT_FIELD_NAME = "profiles";

    public ListImpl() {
      super(DEFAULT_FIELD_NAME);
    }

    public ListImpl(List<CapabilitiesProfile> profiles) {
      this();
      profiles.stream().map(CapabilitiesProfileField::new).forEach(this::add);
    }

    @Override
    public Callable<CapabilitiesProfileField> getCreateListEntryCallable() {
      return CapabilitiesProfileField::new;
    }
  }
}
