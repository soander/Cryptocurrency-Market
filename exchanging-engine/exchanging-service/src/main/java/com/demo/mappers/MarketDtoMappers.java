package com.demo.mappers;

import com.demo.domain.Market;
import com.demo.dto.MarketDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MarketDtoMappers {

    MarketDtoMappers INSTANCE = Mappers.getMapper(MarketDtoMappers.class);

    // Market -> MarketDto
    Market toConvertEntity(MarketDto source);

    // MarketDto -> Market
    MarketDto toConvertDto(Market source);

    // List<MarketDto> -> List<Market>
    List<Market> toConvertEntity(List<MarketDto> source);

    // List<Market> -> List<MarketDto>
    List<MarketDto> toConvertDto(List<Market> source);
}
