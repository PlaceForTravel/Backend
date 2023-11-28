package com.traveler.core.service;

import com.traveler.core.dto.BoardListResponseDTO;
import com.traveler.core.dto.BoardPlaceListResponseDTO;
import com.traveler.core.entity.*;
import com.traveler.core.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SavedBoardRepository savedBoardRepository;
    private final BoardPlaceRepository boardPlaceRepository;
    private final BoardRepository boardRepository;
    private final SavedBoardPlaceRepository savedBoardPlaceRepository;

    public UserServiceImpl(UserRepository userRepository, BoardRepository boardRepository, SavedBoardRepository savedBoardRepository, BoardPlaceRepository boardPlaceRepository, BoardRepository boardRepository1, SavedBoardPlaceRepository savedBoardPlaceRepository) {
        this.userRepository = userRepository;
        this.savedBoardRepository = savedBoardRepository;
        this.boardPlaceRepository = boardPlaceRepository;
        this.boardRepository = boardRepository;
        this.savedBoardPlaceRepository = savedBoardPlaceRepository;
    }

    public Page<BoardListResponseDTO> savedBoardPaging(String userId, Pageable pageable){
        int page = pageable.getPageNumber()-1;
        int pageLimit = 5;
        User user = userRepository.findById(userId).orElse(null);
        Page<SavedBoard> savedBoardPages = savedBoardRepository.findAllByUser(user, PageRequest.of(page,pageLimit, Sort.by(Sort.Direction.DESC,"regDate")));
        Page<BoardListResponseDTO> boardListResponseDTOs = savedBoardPages.map(
                savedBoardPage -> {
                    Board board = savedBoardPage.getBoard();
                    return new BoardListResponseDTO(board);
                });

        return boardListResponseDTOs;
    }

    public Page<BoardPlaceListResponseDTO> savedBoardPlacePaging(String userId, Pageable pageable){
        int page = pageable.getPageNumber()-1; // 현재페이지 반환
        int pagelimit = 5;
        User user = userRepository.findById(userId).orElse(null);
        Page<SavedBoardPlace> savedBoardPlacePages = savedBoardPlaceRepository.findAllByUser(user, PageRequest.of(page, pagelimit, Sort.by(Sort.Direction.DESC,"regDate")));

        Page<BoardPlaceListResponseDTO> boardPlaceListResponseDTOs = savedBoardPlacePages.map(
                savedBoardPlacePage -> {
                    BoardPlace boardPlace = savedBoardPlacePage.getBoardPlace();
                    return new BoardPlaceListResponseDTO(boardPlace);
                });

        return boardPlaceListResponseDTOs;
    }


}
