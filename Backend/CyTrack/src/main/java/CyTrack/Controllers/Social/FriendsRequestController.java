package CyTrack.Controllers.Social;

import CyTrack.Responses.Util.ErrorResponse;
import CyTrack.Responses.Social.FriendRequestIDResponse;
import CyTrack.Responses.Social.FriendRequestResponse;
import CyTrack.Entities.FriendRequest;
import CyTrack.Entities.User;
import CyTrack.Services.Social.FriendsService;
import CyTrack.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Friend Requests", description = "Endpoints for sending, accepting, declining, and viewing friend requests")
@RestController
@RequestMapping("/{userID}/request")
public class FriendsRequestController {
    private final UserService userService;
    private final FriendsService friendsService;

    @Autowired
    public FriendsRequestController(UserService userService, FriendsService friendsService) {
        this.userService = userService;
        this.friendsService = friendsService;
    }

    //Sends a friend request, by inputting a person's username.
    @Operation(
            summary = "Send a friend request",
            description = "Send a friend request to a user by inputting their username",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Friend request sent",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FriendRequestIDResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid username",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Already friends",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request already sent",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request already received",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping("")
    public ResponseEntity<?> sendFriendRequest(@PathVariable Long userID, @RequestBody Map<String, String> friendUser) {
        String friendUsername = friendUser.get("friendUsername");
        if (friendUsername == null || friendUsername.isEmpty()) {
            ErrorResponse response = new ErrorResponse("error", 400, "Invalid username", "Username cannot be null or empty");
            return ResponseEntity.status(400).body(response);
        }

        Optional<User> friend = userService.findByUserName(friendUsername);
        if (friend.isPresent()) {
            if (friend.get().getUserID().equals(userID)) {
                ErrorResponse response = new ErrorResponse("error", 400, "Invalid request", "You cannot send a friend request to yourself");
                return ResponseEntity.status(400).body(response);
            }

            if (friendsService.checkIfFriends(userID, friend.get().getUserID())) {
                ErrorResponse response = new ErrorResponse("error", 400, "Already friends", "You are already friends with this user");
                return ResponseEntity.status(400).body(response);
            }

            if (friendsService.checkIfRequestSent(userID, friend.get().getUserID())) {
                ErrorResponse response = new ErrorResponse("error", 400, "Request already sent", "You have already sent a friend request to this user");
                return ResponseEntity.status(400).body(response);
            }

            if (friendsService.checkIfRequestReceived(friend.get().getUserID(), userID)) {
                ErrorResponse response = new ErrorResponse("error", 400, "Request already received", "This user already has a pending friend request from you");
                return ResponseEntity.status(400).body(response);
            }

            FriendRequest friendRequest = friendsService.sendFriendRequest(friend.get().getUserID(), userID);
            FriendRequestIDResponse response = new FriendRequestIDResponse("success", friendRequest.getFriendRequestID(), "Friend request sent");

            return ResponseEntity.status(201).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
        return ResponseEntity.status(404).body(response);
    }
    @Operation(
            summary = "Accept a friend request",
            description = "Accept a friend request by inputting the request ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Friend request accepted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FriendRequestIDResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request ID",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Friend request not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PutMapping("")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long userID, @RequestBody Map<String, Long> friendRequestID) {
        Long requestID = friendRequestID.get("friendRequestID");
        if (requestID == null) {
            ErrorResponse response = new ErrorResponse("error", 400, "Invalid request ID", "Request ID cannot be null");
            return ResponseEntity.status(400).body(response);
        }

        Optional<FriendRequest> friendRequest = friendsService.findFriendRequestByID(requestID);

        if (friendRequest.isPresent()) {
            FriendRequest friendRequest1 = friendRequest.get();

            if (!friendRequest1.getStatus().equals(FriendRequest.RequestStatus.PENDING) || !friendRequest1.getReceiver().getUserID().equals(userID)) {
                ErrorResponse response = new ErrorResponse("error", 400, "Invalid request", "Friend request is not pending");
                return ResponseEntity.status(400).body(response);
            }

            friendsService.acceptFriendRequest(friendRequest1);

            FriendRequestIDResponse response = new FriendRequestIDResponse("success", requestID, "Friend request accepted");
            return ResponseEntity.status(200).body(response);


        }
        ErrorResponse response = new ErrorResponse("error", 404, "Friend request not found", "Friend request not found");
        return ResponseEntity.status(404).body(response);
    }

    @Operation(
            summary = "Gets incoming friend requests",
            description = "Get all incoming friend requests for a user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Incoming friend requests found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FriendRequestResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/incoming")
    public ResponseEntity<?> getIncomingFriendRequests(@PathVariable Long userID){
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()){
            List<FriendRequest> friendRequests = friendsService.getIncomingFriendRequests(userID);
            List<FriendRequestResponse.FriendRequestData> friendRequestDataList = friendRequests.stream()
                    .map(friendRequest -> new FriendRequestResponse.FriendRequestData(
                            friendRequest.getSender().getFirstName(),
                            friendRequest.getSender().getUsername(),
                            friendRequest.getSender().getUserID(),
                            friendRequest.getFriendRequestID()
                    ))
                    .toList();
            FriendRequestResponse response = new FriendRequestResponse("success", friendRequestDataList, "Incoming friend requests found");
            return ResponseEntity.status(200).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
        return ResponseEntity.status(404).body(response);
    }

    @Operation(
            summary = "Gets outgoing friend requests",
            description = "Get all outgoing friend requests for a user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Outgoing friend requests found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FriendRequestResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/outgoing")
    public ResponseEntity<?> getOutgoingFriendRequests(@PathVariable Long userID){
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()){
            List<FriendRequest> friendRequests = friendsService.getOutgoingFriendRequests(userID);
            List<FriendRequestResponse.FriendRequestData> friendRequestDataList = friendRequests.stream()
                    .map(friendRequest -> new FriendRequestResponse.FriendRequestData(
                            friendRequest.getReceiver().getFirstName(),
                            friendRequest.getReceiver().getUsername(),
                            friendRequest.getReceiver().getUserID(),
                            friendRequest.getFriendRequestID()
                    ))
                    .toList();
            FriendRequestResponse response = new FriendRequestResponse("success", friendRequestDataList, "Outgoing friend requests found");
            return ResponseEntity.status(200).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
        return ResponseEntity.status(404).body(response);
    }

    @Operation(
            summary = "Decline a friend request",
            description = "Decline a friend request by inputting the request ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Friend request declined",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FriendRequestIDResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request ID",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Friend request not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @DeleteMapping("/{friendRequestID}")
    public ResponseEntity<?> declineFriendRequest(@PathVariable Long userID, @PathVariable Long friendRequestID) {
        if (friendRequestID == null) {
            ErrorResponse response = new ErrorResponse("error", 400, "Invalid request ID", "Request ID cannot be null");
            return ResponseEntity.status(400).body(response);
        }

        Optional<FriendRequest> friendRequest = friendsService.findFriendRequestByID(friendRequestID);

        if (friendRequest.isPresent()) {
            FriendRequest friendRequest1 = friendRequest.get();

            if (!friendRequest1.getStatus().equals(FriendRequest.RequestStatus.PENDING) || !friendRequest1.getReceiver().getUserID().equals(userID)) {
                ErrorResponse response = new ErrorResponse("error", 400, "Invalid request", "Friend request is not pending");
                return ResponseEntity.status(400).body(response);
            }

            friendsService.declineFriendRequest(friendRequest1);

            FriendRequestIDResponse response = new FriendRequestIDResponse("success", friendRequestID, "Friend request declined");
            return ResponseEntity.status(200).body(response);

        }
        ErrorResponse response = new ErrorResponse("error", 404, "Friend request not found", "Friend request not found");
        return ResponseEntity.status(404).body(response);
    }
}
