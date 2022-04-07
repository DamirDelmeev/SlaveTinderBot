package com.example.shippingbotserver.dao;

import com.example.shippingbotserver.entity.Lover;
import com.example.shippingbotserver.model.LoverModel;
import com.example.shippingbotserver.repository.LoverModelRepository;
import com.example.shippingbotserver.repository.LoverRepository;
import com.example.shippingbotserver.utils.LoverShower;
import com.example.shippingbotserver.view.FormHandler;
import com.example.shippingbotserver.view.FormLoverModel;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

class DaoProcessingTest {

    @Mock
    private LoverRepository loverRepository;

    @Mock
    private LoverModelRepository modelRepository;

    @Mock
    private FormHandler formHandler;

    private final DaoProcessing daoProcessing;

    public DaoProcessingTest() {
        MockitoAnnotations.openMocks(this);
        this.daoProcessing = new DaoProcessing(loverRepository, modelRepository, formHandler, new LoverShower());
    }

    @Test
    void findLoverByIdTrue() {
        Lover orig = new Lover(123L, "Антон", "Сударь", "Громкий", "girl",
                new HashSet<>(), new HashSet<>(), new HashSet<>(), null);
        Optional<Lover> person = Optional.of(orig);

        given(loverRepository.findById(1L)).willReturn(person);

        FormLoverModel response = daoProcessing.findLoverById(1L);

        assertTrue((response.getLover().equals(orig) &&
                response.getStatus().isEmpty()));
    }

    @Test
    void findLoverByIdException() {
        given(loverRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> daoProcessing.findLoverById(1L)).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    void getFavoriteTrue() {
        Lover orig = new Lover(123L, "Антон", "Сударь", "Громкий", "girl",
                new HashSet<>(), new HashSet<>(), new HashSet<>(), null);
        Lover trueResponse = new Lover(123L, "Катя", "Сударыня", "1", "1", null, null, null, null);
        orig.getLike().add(new LoverModel(trueResponse));
        Optional<Lover> person = Optional.of(orig);

        given(loverRepository.findById(1L)).willReturn(person);
        given(formHandler.getName()).willReturn("Катя");

        FormLoverModel formLoverModel = daoProcessing.getFavorite(1L, "Вправо");
        assertEquals(trueResponse, formLoverModel.getLover());
        assertEquals("Любимъ вами", formLoverModel.getStatus());
    }

    @Test
    void getFavoriteException() {
        given(loverRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> daoProcessing.getFavorite(1L, "Вправо")).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    void getQuestionnaireTrue() {
        List<LoverModel> heapLoverModels = new ArrayList<>();
        for (long i = 3; i < 18; i++) {
            heapLoverModels.add(new LoverModel(i, "name " + i, "girl", "", "boy"));
        }
        Lover orig = new Lover(1L, "Антон", "boy", "Громкий", "girl",
                new HashSet<>(), new HashSet<>(), new HashSet<>(), null);
        given(modelRepository.findAll()).willReturn(heapLoverModels);
        given(loverRepository.findById(1L)).willReturn(Optional.of(orig));
        FormLoverModel f = daoProcessing.getQuestionnaire(1L);
        assertEquals(f.getLover().getId(), heapLoverModels.get(0).getId());
    }

    @Test
    void getQuestionnaireEmpty() {
        List<LoverModel> heapLoverModels = new ArrayList<>();
        for (long i = 3; i < 18; i++) {
            heapLoverModels.add(new LoverModel(i, "name " + i, "boy", "", "boy"));
        }
        Lover orig = new Lover(1L, "Антон", "boy", "Громкий", "girl",
                new HashSet<>(), new HashSet<>(), new HashSet<>(), null);
        given(modelRepository.findAll()).willReturn(heapLoverModels);
        given(loverRepository.findById(1L)).willReturn(Optional.of(orig));
        FormLoverModel f = daoProcessing.getQuestionnaire(1L);
        assertEquals(-1, (long) f.getLover().getId());
    }

    @Test
    void getQuestionnaireException() {
        given(loverRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> daoProcessing.getQuestionnaire(1L)).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    void likeTrue() {
        List<LoverModel> heapLoverModels = new ArrayList<>();
        for (long i = 3; i < 18; i++) {
            heapLoverModels.add(new LoverModel(i, "name " + i, "girl", "", "boy"));
        }
        Lover orig = new Lover(1L, "Антон", "boy", "Громкий", "girl",
                new HashSet<>(), new HashSet<>(), new HashSet<>(), null);
        given(modelRepository.findAll()).willReturn(heapLoverModels);
        given(loverRepository.findById(1L)).willReturn(Optional.of(orig));
        FormLoverModel f = daoProcessing.like(orig);
        assertEquals(4, f.getLover().getId());
    }

    @Test
    void disLikeTrue() {
        List<LoverModel> heapLoverModels = new ArrayList<>();
        for (long i = 3; i < 18; i++) {
            heapLoverModels.add(new LoverModel(i, "name " + i, "girl", "", "boy"));
        }
        Lover orig = new Lover(1L, "Антон", "boy", "Громкий", "girl",
                new HashSet<>(), new HashSet<>(), new HashSet<>(), null);
        given(modelRepository.findAll()).willReturn(heapLoverModels);
        given(loverRepository.findById(1L)).willReturn(Optional.of(orig));
        FormLoverModel f = daoProcessing.dislike(orig);
        assertEquals(4, f.getLover().getId());
    }

    @Test
    void LikesException() {
        Lover orig = new Lover(1L, "Антон", "boy", "Громкий", "girl",
                new HashSet<>(), new HashSet<>(), new HashSet<>(), null);
        given(loverRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> daoProcessing.like(orig)).isExactlyInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> daoProcessing.dislike(orig)).isExactlyInstanceOf(RuntimeException.class);
    }
}