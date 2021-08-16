package FileExplorer.security.services;


import FileExplorer.model.History;
import FileExplorer.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {



    @Autowired
    HistoryRepository historyRepository;


    public List<History> getAllHistory(){return historyRepository.findAllByOrderByIdAsc();}


}
