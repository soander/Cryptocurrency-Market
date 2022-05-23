package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.AdminBank;
//import com.demo.dto.AdminBankDto;
import com.demo.mapper.AdminBankMapper;
//import com.demo.mappers.AdminBankDtoMappers;
import com.demo.service.AdminBankService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class AdminBankServiceImpl extends ServiceImpl<AdminBankMapper, AdminBank> implements AdminBankService{

    @Override
    public Page<AdminBank> findByPage(Page<AdminBank> page, String bankCard) {
        return page(page,new LambdaQueryWrapper<AdminBank>()
                                        .like(!StringUtils.isEmpty(bankCard),AdminBank::getBankCard ,bankCard));
    }

//    @Override
//    public List<AdminBankDto> getAllAdminBanks() {
//        List<AdminBank> adminBanks = list(new LambdaQueryWrapper<AdminBank>().eq(AdminBank::getStatus, 1));
//        if (CollectionUtils.isEmpty(adminBanks)){
//            return Collections.emptyList() ;
//        }
//        List<AdminBankDto> adminBankDtos = AdminBankDtoMappers.INSTANCE.toConvertDto(adminBanks);
//        return adminBankDtos;
//    }
}
