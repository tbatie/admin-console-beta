package org.codice.ddf.admin.fields;

import java.util.List;
import java.util.concurrent.Callable;

import org.codice.ddf.admin.api.Field;
import org.codice.ddf.admin.services.api.Capability;
import org.codice.ddf.admin.common.fields.base.BaseListField;
import org.codice.ddf.admin.common.fields.base.BaseObjectField;
import org.codice.ddf.admin.common.fields.base.scalar.StringField;

import com.google.common.collect.ImmutableList;

public class CapabilityField extends BaseObjectField {

  public static final String FIELD_TYPE = "Capability";

  public static final String DEFAULT_FIELD_NAME = "capability";

  public static final String DESCRIPTION = "Defines a type of functionality that can be started";

  private StringField id;
  private StringField name;
  private StringField description;

  public CapabilityField() {
    super(DEFAULT_FIELD_NAME, FIELD_TYPE, DESCRIPTION);
    id = new StringField("id");
    name = new StringField("name");
    description = new StringField("description");
  }

  public CapabilityField(Capability capability) {
    this();
    id.setValue(capability.getId());
    name.setValue(capability.getName());
    description.setValue(capability.getDescription());
  }

  public String id() { return id.getValue(); }

  public String name() {
    return name.getValue();
  }

  public String description() {
    return description.getValue();
  }

  public StringField getNameField() {
    return name;
  }

  public StringField getDescriptionField() {
    return description;
  }

  @Override
  public List<Field> getFields() {
    return ImmutableList.of(id, name, description);
  }

  public static class ListImpl extends BaseListField<CapabilityField> {

    public static final String DEFAULT_FIELD_NAME = "capabilities";

    public ListImpl() {
      super(DEFAULT_FIELD_NAME);
    }

    public ListImpl(List<Capability> capabilities) {
      this();
      capabilities.stream().map(CapabilityField::new).forEach(this::add);
    }

    @Override
    public Callable<CapabilityField> getCreateListEntryCallable() {
      return CapabilityField::new;
    }
  }
}
