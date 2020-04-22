package io.chainmind.myriad.domain.dto.validation;

import javax.validation.groups.Default;

public interface VoucherConfigValid extends Default {

    interface Creator extends VoucherConfigValid, Default {}
    interface Updater extends VoucherConfigValid{}
    interface VoucherCreator extends VoucherConfigValid{}
}
