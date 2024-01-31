package com.traveler.core.service;

import com.traveler.core.dto.*;
import com.traveler.core.entity.*;
import com.traveler.core.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final PlaceRepository placeRepository;
    private final BoardPlaceRepository boardPlaceRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final SavedBoardRepository savedBoardRepository;
    private final SavedBoardPlaceRepository savedBoardPlaceRepository;
    private final FCMNotificationService fcmNotificationService;
    private final S3Service s3Service;

    public BoardService(BoardRepository boardRepository, PlaceRepository placeRepository, BoardPlaceRepository boardPlaceRepository, ImageRepository imageRepository, UserRepository userRepository, SavedBoardRepository savedBoardRepository, SavedBoardPlaceRepository savedBoardPlaceRepository, FCMNotificationService fcmNotificationService, S3Service s3Service) {
        this.boardRepository = boardRepository;
        this.placeRepository = placeRepository;
        this.boardPlaceRepository = boardPlaceRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;

        this.savedBoardRepository = savedBoardRepository;
        this.savedBoardPlaceRepository = savedBoardPlaceRepository;
        this.fcmNotificationService = fcmNotificationService;

        this.s3Service = s3Service;
    }

    public Page<BoardListResponseDTO> listPaging(Pageable pageable, String userId) {
        int page = pageable.getPageNumber() - 1; // 현재페이지 반환
        int pagelimit = 5;

        Page<Board> boardPages = boardRepository.findAllByDeletedDateIsNull(PageRequest.of(page, pagelimit, Sort.by(Sort.Direction.DESC, "regDate")));
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Page<BoardListResponseDTO> boardListResponseDTOs = boardPages.map(
                    (boardPage) -> {
                        if (savedBoardRepository.findByUserAndBoard(user.get(), boardPage).isPresent()) {
                            return new BoardListResponseDTO(boardPage, true);
                        }
                        return new BoardListResponseDTO(boardPage, false);
                    });
            return boardListResponseDTOs;
        }
        Page<BoardListResponseDTO> boardListResponseDTOs = boardPages.map(
                (boardPage) -> new BoardListResponseDTO(boardPage, false));
        return boardListResponseDTOs;
    }
//    public Page<BoardListResponseDTO> listPaging(Pageable pageable, String userId) {
//        int pagelimit = 5;
//
//        // 현재 페이지 번호를 가져오기 위해 pageable.previousOrFirst().getPageNumber()를 사용
//        Page<Board> boardPages = boardRepository.findAllByDeletedDateIsNull(
//                PageRequest.of(pageable.previousOrFirst().getPageNumber(), pagelimit, Sort.by(Sort.Direction.DESC, "regDate"))
//        );
//
//        // 주어진 userId로 사용자를 찾음
//        Optional<User> user = userRepository.findById(userId);
//
//        // Stream을 사용하여 각 Board에 대한 BoardListResponseDTO를 생성하고 리스트로 수집
//        List<BoardListResponseDTO> boardListResponseDTOs = boardPages.getContent().stream()
//                .map(boardPage -> {
//                    // user.map(...)을 사용하여 user가 존재하면 저장된 Board를 찾아보고, 그 결과를 isSaved에 저장
//                    boolean isSaved = user.map(u -> savedBoardRepository.findByUserAndBoard_NotDeleted(u, boardPage))
//                            .map(Optional::isPresent)
//                            .orElse(false);
//
//                    // BoardListResponseDTO를 생성하고 반환
//                    return new BoardListResponseDTO(boardPage, isSaved);
//                })
//                .collect(Collectors.toList());
//
//        // 새로운 Page 객체를 생성하여 BoardListResponseDTO 리스트와 메타 정보를 포함
//        return new PageImpl<>(boardListResponseDTOs, pageable, boardPages.getTotalElements());
//    }


//    public Page<BoardListResponseDTO> placePaging(Pageable pageable,String searchWord,String userId){
//        int page = pageable.getPageNumber()-1; // 현재페이지 반환
//        int pagelimit = 5;
//        Page<Board> boardPages = boardRepository.findAllBySearchWord(searchWord, PageRequest.of(page, pagelimit, Sort.by(Sort.Direction.DESC,"regDate")));
//        Optional<User> user = userRepository.findById(userId);
//        Page<BoardListResponseDTO> boardListResponseDTOs = boardPages.map(
//                (boardPage) -> {
//                    if(savedBoardRepository.findByUserAndBoard_NotDeleted(user.get(),boardPage).isPresent()){
//                        return new BoardListResponseDTO(boardPage, true);
//                    }
//                    return new BoardListResponseDTO(boardPage, false);
//                });
//
//        return boardListResponseDTOs;
//    }
//    public Page<BoardPlaceListResponseDTO> boardPlacePaging(Pageable pageable, String placeName){
//        int page = pageable.getPageNumber()-1; // 현재페이지 반환
//        int pagelimit = 5;
//        Page<BoardPlace> boardPlacePages = boardPlaceRepository.findBoardPlaceByPlacePlaceName(placeName, PageRequest.of(page, pagelimit, Sort.by(Sort.Direction.DESC,"regDate")));
//
//        Page<BoardPlaceListResponseDTO> boardPlaceListResponseDTOs = boardPlacePages.map(
//                boardPlacePage -> new BoardPlaceListResponseDTO(boardPlacePage));
//
//        return boardPlaceListResponseDTOs;
//    }

    public void save(BoardSaveDTO boardSaveDTO, List<MultipartFile> multipartFiles) {
        User user = userRepository.findById(boardSaveDTO.getUserId()).orElse(null);
        Board board = boardSaveDTO.toBoardEntity(user);
        List<PlaceRequestDTO> places = boardSaveDTO.getPlaces();
        List<BoardPlace> boardPlaces = new ArrayList<>();
        boardRepository.save(board);

        places.forEach(place -> {
            Place placeEntity = place.toPlaceEntity();
            placeRepository.save(placeEntity);
            BoardPlace boardPlace = BoardPlace.builder().board(board).place(placeEntity).regDate(board.getRegDate()).build();
            boardPlaceRepository.save(boardPlace);
            boardPlaces.add(boardPlace);
            String fileName = board.getBoardId() + "_" + placeEntity.getPlaceId() + "_" + board.getRegDate().toLocalDate().toString();
            int index = place.getImgIndex();
            try {
                String imgUrl = s3Service.upload(multipartFiles.get(index), fileName);
                Image img = place.toImageEntity(imgUrl, boardPlace);
                imageRepository.save(img);
                boardPlace.setImage(img);
                boardPlaceRepository.save(boardPlace);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }

    public void edit(int boardId, BoardEditDTO boardEditDTO) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board.getUser().getUserId().equals(boardEditDTO.getUserId())) {

            board.setContent(boardEditDTO.getContent());
            board.setModifiedDate(LocalDateTime.now());
            boardRepository.save(board);
        } else {

            //오류 날리기
        }

    }

    public void delete(int boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        board.setDeletedDate(LocalDateTime.now());
        boardRepository.save(board);
    }

    public boolean like(int boardId, String userId) {

        Board board = boardRepository.findById(boardId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        Optional<SavedBoard> savedBoard = savedBoardRepository.findByUserAndBoard(user, board);
        if (savedBoard.isPresent()) {
            board.setLikeCount(board.getLikeCount() - 1);
            boardRepository.save(board);

            savedBoardRepository.delete(savedBoard.get());
            return false;
        }

        board.setLikeCount(board.getLikeCount() + 1);
        boardRepository.save(board);

        SavedBoard newSavedBoard = new SavedBoard(board, user, LocalDateTime.now());
        savedBoardRepository.save(newSavedBoard);
        return true;

    }

    public void likeBoardPlace(int boardPlaceId, String userId) {
        BoardPlace boardPlace = boardPlaceRepository.findById(boardPlaceId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        SavedBoardPlace savedBoardPlace = new SavedBoardPlace(user, boardPlace, LocalDateTime.now());

        savedBoardPlaceRepository.save(savedBoardPlace);
    }

    public BoardDetailResponseDTO showBoardDetail(int boardId, String userId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        List<BoardPlace> boardPlace = boardPlaceRepository.findBoardPlacesByBoardBoardId(boardId);
        if (board != null && boardPlace != null) {
            if (user != null) {
                if (savedBoardRepository.findByUserAndBoard(user, board).isPresent()) {
                    BoardDetailResponseDTO boardDetailResponseDTO = new BoardDetailResponseDTO(board, boardPlace, true);
                    return boardDetailResponseDTO;
                }
                BoardDetailResponseDTO boardDetailResponseDTO = new BoardDetailResponseDTO(board, boardPlace, false);
                return boardDetailResponseDTO;
            } else {
                BoardDetailResponseDTO boardDetailResponseDTO = new BoardDetailResponseDTO(board, boardPlace, false);
                return boardDetailResponseDTO;
            }
        } else return null;
    }

    public void likeNoti(int boardId, UserDTO userDTO) {
        Optional<Board> board = boardRepository.findById(boardId);
        Map<String, String> data = new HashMap<String, String>();
        data.put("boardId", Integer.toString(boardId));
        if(userDTO.getUserId()!=board.get().getUser().getUserId()){
        FCMNotificationRequestDTO fcmNotificationRequestDTO = FCMNotificationRequestDTO.builder()
                .body(userDTO.getUserId() + "님이 당신의 게시물을 좋아요 눌렀습니다.")
                .title("좋아요")
                .userId(board.get().getUser().getUserId())
                .data(data)
                .build();
        fcmNotificationService.sendNotificationByToken(fcmNotificationRequestDTO);}
    }
}
