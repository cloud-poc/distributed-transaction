package org.akj.springboot.tx.service;

import org.akj.springboot.tx.constant.Constant;
import org.akj.springboot.tx.dto.CustomerAuditDto;
import org.akj.springboot.tx.entity.audit.CustomerAudit;
import org.akj.springboot.tx.entity.audit.CustomerAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Date;

@Service
public class CustomerAuditService {

    @Autowired
    private CustomerAuditRepository repository;

    public CustomerAuditDto findCustomerAuditLog(Date date, int pageNo) {
        if (pageNo < 0) throw new InvalidParameterException("page number could not be less than 0");
        CustomerAuditDto dto = new CustomerAuditDto();
        Pageable pageable = PageRequest.of(pageNo, Constant.PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createDate"
        ));

        Page<CustomerAudit> page = repository.findAllByDate(date, pageable);
        if (null != page && page.getTotalElements() > 0) {
            dto.setPageNo(pageNo);
            dto.setTotalRecords(page.getTotalElements());
            dto.setHasNextPage(page.getTotalPages() < pageNo);
            page.getContent().removeIf(item -> {
                return item.getId() < 5;
            });
            dto.setAuditLoglist(page.getContent());
        }

        return dto;
    }
}
