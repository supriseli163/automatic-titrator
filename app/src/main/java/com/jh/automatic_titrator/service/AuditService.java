package com.jh.automatic_titrator.service;

import com.jh.automatic_titrator.common.db.AuditHelper;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.entity.common.Audit;

/**
 * Created by apple on 2016/12/19.
 */
public class AuditService {

    public static void addAuditService(String operator,
                                       String fragment,
                                       String subFragment,
                                       String event,
                                       long date,
                                       AuditHelper auditHelper) {
        Audit audit = new Audit();
        audit.setOperator(operator);
        audit.setFragment(fragment);
        audit.setSubFragment(subFragment);
        audit.setEvent(event);
        audit.setDate(TimeTool.dateFormatter(date, "yyyy-MM-dd HH:mm:ss"));

        auditHelper.addAudit(audit);
    }
}
