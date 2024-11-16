package faang.school.postservice.controller;

import faang.school.postservice.dto.album.AlbumCreateUpdateDto;
import faang.school.postservice.dto.album.AlbumDto;
import faang.school.postservice.dto.album.AlbumFilterDto;
import faang.school.postservice.service.album.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Tag(
        name = "API for managing posts' albums",
        description = "Provides endpoints for creating, updating, retrieving, and deleting albums associated with posts."
)
@Validated
@RestController
@RequestMapping("/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @Operation(summary = "Create a new album", description = "Creates an empty album with the given title and description.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Album was created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid title or description provided"),
            @ApiResponse(responseCode = "401", description = "User is unauthorized or does not exist in data base")
    })
    @PostMapping
    public ResponseEntity<AlbumDto> createAlbum(
            @RequestHeader("x-user-id")
            @Min(value = 1, message = "User ID must be greater than 0!")
            @Parameter(description = "ID of user who sent the request", required = true) long userId,
            @RequestBody @Valid AlbumCreateUpdateDto createDto
    ) {
        AlbumDto responseDto = albumService.createAlbum(createDto);
        URI albumUri = URI.create("/albums/%d".formatted(responseDto.getId()));
        return ResponseEntity.created(albumUri).body(responseDto);
    }

    @Operation(summary = "Add a post to an album", description = "Associates a post with an album.")
    @PostMapping("/{albumId}/posts/{postId}")
    public ResponseEntity<AlbumDto> addPostToAlbum(
            @RequestHeader("x-user-id")
            @Min(value = 1, message = "User ID must be greater than 0!")
            @Parameter(description = "ID of user who sent the request") long userId,
            @PathVariable @Min(value = 1, message = "Album ID must be greater than 0!") long albumId,
            @PathVariable @Min(value = 1, message = "Post ID must be greater than 0!") long postId
    ) {
        AlbumDto responseDto = albumService.addPostToAlbum(albumId, postId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Remove a post from an album", description = "Deletes the association of a post with an album.")
    @DeleteMapping("/{albumId}/posts/{postId}")
    public ResponseEntity<Void> deletePostFromAlbum(
            @RequestHeader("x-user-id")
            @Min(value = 1, message = "User ID must be greater than 0!")
            @Parameter(description = "ID of user who sent the request") long userId,
            @PathVariable @Min(value = 1, message = "Album ID must be greater than 0!") long albumId,
            @PathVariable @Min(value = 1, message = "Post ID must be greater than 0!") long postId
    ) {
        albumService.deletePostFromAlbum(albumId, postId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Mark an album as favorite", description = "Adds an album to the user's list of favorite albums.")
    @PostMapping("/{albumId}/favorite")
    public ResponseEntity<Void> addAlbumToFavorites(
            @RequestHeader("x-user-id")
            @Min(value = 1, message = "User ID must be greater than 0!")
            @Parameter(description = "ID of user who sent the request") long userId,
            @PathVariable @Min(value = 1, message = "Album ID must be greater than 0!") long albumId
    ) {
        albumService.addAlbumToFavorites(albumId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Unmark an album as favorite", description = "Removes an album from the user's list of favorite albums.")
    @DeleteMapping("/{albumId}/favorite")
    public ResponseEntity<Void> deleteAlbumFromFavorites(
            @RequestHeader("x-user-id")
            @Min(value = 1, message = "User ID must be greater than 0!")
            @Parameter(description = "ID of user who sent the request") long userId,
            @PathVariable @Min(value = 1, message = "Album ID must be greater than 0!") long albumId
    ) {
        albumService.deleteAlbumFromFavorites(albumId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retrieve an album by ID", description = "Fetches details of an album by its ID.")
    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumDto> getAlbumById(
            @RequestHeader("x-user-id")
            @Min(value = 1, message = "User ID must be greater than 0!")
            @Parameter(description = "ID of user who sent the request") long userId,
            @PathVariable @Min(value = 1, message = "Album ID must be greater than 0!") long albumId
    ) {
        AlbumDto responseDto = albumService.getAlbumById(albumId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Filter all albums", description = "Filters all albums based on criteria.")
    @PostMapping("/filter")
    public ResponseEntity<List<AlbumDto>> getAllAlbums(
            @RequestHeader("x-user-id")
            @Min(value = 1, message = "User ID must be greater than 0!")
            @Parameter(description = "ID of user who sent the request") long userId,
            @RequestBody AlbumFilterDto filterDto
    ) {
        List<AlbumDto> filteredAlbums = albumService.getAllAlbums(filterDto);
        return ResponseEntity.ok(filteredAlbums);
    }

    @Operation(summary = "Filter user's albums", description = "Filters user's albums based on criteria.")
    @PostMapping("/user/filter")
    public ResponseEntity<List<AlbumDto>> getUserAlbums(
            @RequestHeader("x-user-id")
            @Min(value = 1, message = "User ID must be greater than 0!")
            @Parameter(description = "ID of user who sent the request") long userId,
            @RequestBody AlbumFilterDto filterDto
    ) {
        List<AlbumDto> filteredAlbums = albumService.getUserAlbums(filterDto);
        return ResponseEntity.ok(filteredAlbums);
    }

    @Operation(summary = "Filter user's favorite albums", description = "Filters user's favorite albums based on criteria.")
    @PostMapping("/user/favorite/filter")
    public ResponseEntity<List<AlbumDto>> getUserFavoriteAlbums(
            @RequestHeader("x-user-id")
            @Min(value = 1, message = "User ID must be greater than 0!")
            @Parameter(description = "ID of user who sent the request") long userId,
            @RequestBody AlbumFilterDto filterDto
    ) {
        List<AlbumDto> filteredAlbums = albumService.getUserFavoriteAlbums(filterDto);
        return ResponseEntity.ok(filteredAlbums);
    }

    @Operation(summary = "Update an album", description = "Updates an album's details")
    @PatchMapping("/{albumId}")
    public ResponseEntity<AlbumDto> updateAlbum(
            @RequestBody @Valid AlbumCreateUpdateDto updateDto,
            @PathVariable @Min(1) long albumId
    ) {
        AlbumDto responseDto = albumService.updateAlbum(albumId, updateDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Delete an album", description = "Deletes an album")
    @DeleteMapping("/{albumId}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable @Min(1) long albumId) {
        albumService.deleteAlbum(albumId);
        return ResponseEntity.noContent().build();
    }
}