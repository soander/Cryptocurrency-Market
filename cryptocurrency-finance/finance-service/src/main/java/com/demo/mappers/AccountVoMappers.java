package com.demo.mappers;

import com.demo.domain.Account;
import com.demo.vo.AccountVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountVoMappers {

    AccountVoMappers INSTANCE = Mappers.getMapper(AccountVoMappers.class) ;

    // Account -> AccountVo
    AccountVo toConvertVo(Account source);

    // List<Account> -> List<AccountVo>
    List<AccountVo> toConvertVo(List<Account> source);

    // AccountVo -> Account
    Account toConvertEntity(AccountVo source);

    // List<AccountVo> -> List<Account>
    List<Account> toConvertEntity(List<AccountVo> source);
}
