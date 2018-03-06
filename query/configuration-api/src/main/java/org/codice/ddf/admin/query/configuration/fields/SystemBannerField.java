package org.codice.ddf.admin.query.configuration.fields;

import java.util.List;

import org.codice.ddf.admin.api.Field;
import org.codice.ddf.admin.common.fields.base.BaseObjectField;

public class SystemBannerField extends BaseObjectField {

  public static final String DEFAULT_FIELD_NAME = "systemBanner";

  public static final String FIELD_TYPE_NAME = "SystemBanner";

  public static final String DESCRIPTION = "Contains textual and themeing information about the system banner.";

  // TODO: tbatie - 3/2/18 - Wire up all these fields
  public SystemBannerField() {
    super(DEFAULT_FIELD_NAME, FIELD_TYPE_NAME, DESCRIPTION);
  }

  @Override
  public List<Field> getFields() {
    return null;
  }
}
