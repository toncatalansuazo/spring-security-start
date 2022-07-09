package cl.zeruscode.treip.mapper;

import cl.zeruscode.treip.domain.user.DentalUserDetails;
import cl.zeruscode.treip.domain.user.UserDto;
import cl.zeruscode.treip.entities.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class );

    DentalUserDetails userToUserDetail(UserDto user);

    DentalUserDetails toUserDetails(UserEntity user);

}
