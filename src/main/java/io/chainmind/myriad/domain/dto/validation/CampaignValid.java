package io.chainmind.myriad.domain.dto.validation;

import javax.validation.groups.Default;

public interface CampaignValid extends Default {

    interface Creator extends CampaignValid, Default {}
    interface Updater extends CampaignValid{}
    
}
