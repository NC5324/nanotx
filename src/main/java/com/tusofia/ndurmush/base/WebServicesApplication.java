package com.tusofia.ndurmush.base;

import com.tusofia.ndurmush.business.document.web.NtxDocumentWebService;
import com.tusofia.ndurmush.business.masterdata.web.PartMasterDataWebService;
import com.tusofia.ndurmush.business.partoffer.web.PartOfferCommentWebService;
import com.tusofia.ndurmush.business.partoffer.web.PartOfferWebService;
import com.tusofia.ndurmush.business.partrequest.web.PartRequestWebService;
import com.tusofia.ndurmush.business.user.web.UserWebService;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class WebServicesApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final HashSet<Class<?>> classes = new HashSet<>(super.getClasses());
        classes.add(UserWebService.class);
        classes.add(PartOfferWebService.class);
        classes.add(PartOfferCommentWebService.class);
        classes.add(PartRequestWebService.class);
        classes.add(NtxDocumentWebService.class);
        classes.add(PartMasterDataWebService.class);
        return classes;
    }

}
