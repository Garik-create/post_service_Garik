package faang.school.postservice.mapper;

import faang.school.postservice.dto.post.PostDto;
import faang.school.postservice.model.Post;
import faang.school.postservice.model.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    @Mapping(target = "resourceIds", source = "resources", qualifiedByName = "getResourceIds")
    PostDto toDto(Post project);

    Post toEntity(PostDto projectDto);

    @Named("getResourceIds")
    default List<Long> getResourceIds(List<Resource> resources) {
        if (resources == null) {
            return null;
        }
        return resources.stream().map(Resource::getId).toList();
    }
}