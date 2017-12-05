package de.vaplus.api.entity;

import de.vaplus.api.entity.embeddable.Commissionable;

public interface CommissionActivity extends Activity{

	Commissionable getCommission();

	void setCommission(Commissionable commission);

}
