package su.zencode.testapp04.CategoryLab;

import su.zencode.testapp04.EaptekaRepositories.Offer;
import su.zencode.testapp04.UpdatableOffersFragment;

public interface IOfferLab extends ICategoryLab{
    void getOfferImage(Offer offer, UpdatableOffersFragment offersFragment);
}
