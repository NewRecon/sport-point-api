package ru.newrecon.profile_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.newrecon.profile_service.config.MapstructConfig;
import ru.newrecon.profile_service.dto.CreateProfileRq;
import ru.newrecon.profile_service.dto.CreateProfileRs;
import ru.newrecon.profile_service.dto.GetProfileRs;
import ru.newrecon.profile_service.dto.UpdateProfileRq;
import ru.newrecon.profile_service.dto.UpdateProfileRs;
import ru.newrecon.profile_service.entity.Profile;

@Mapper(config = MapstructConfig.class)
public interface ProfileMapper {

    @Mapping(target = "id", ignore = true)
    Profile map(CreateProfileRq source);

    @Mapping(target = "id", ignore = true)
    Profile map(UpdateProfileRq source);

    CreateProfileRs mapToCreateProfileRs(Profile source);

    UpdateProfileRs mapToUpdateProfileRs(Profile source);

    GetProfileRs mapToGetProfileRs(Profile source);
}
