package fr.univ.givr.mapper;

import fr.univ.givr.model.announcement.Announcement;
import fr.univ.givr.model.announcement.AnnouncementDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper to create instance of announcement from a dto or data class.
 */
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AnnouncementMapper {

    /**
     * Create a new instance of announcement data from the dto announcement.
     *
     * @param announcementDto Announcement dto.
     * @return A new instance of announcement data.
     */
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "account", ignore = true)
    Announcement dtoToAnnouncement(AnnouncementDTO announcementDto);
}
