package uk.gov.digital.ho.hocs.info.domain.entity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.domain.entity.dto.EntityDto;
import uk.gov.digital.ho.hocs.info.domain.entity.dto.GetCaseSummaryFieldsResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EntityResourceTest {

    @Mock
    protected EntityService entityService;
    private EntityResource entityResource;


    @Before
    public void before() {
        entityResource = new EntityResource(entityService);
    }

    @Test
    public void getCaseSummary() {
        String caseType = "C1";
        String simpleName = "name123";
        String data = "{ title: 'Title 321' }";
        Set<Entity> entitiesToReturn = Set.of(new Entity(1L, UUID.randomUUID(), simpleName, data, UUID.randomUUID(), true));

        when(entityService.getBySimpleName("caseType", caseType, "summary")).thenReturn(entitiesToReturn);

        ResponseEntity<GetCaseSummaryFieldsResponse> result = entityResource.getCaseSummary(caseType);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getFields().size()).isEqualTo(1);
        assertThat(result.getBody().getFields().iterator().next()).isEqualTo(simpleName);

        verify(entityService).getBySimpleName("caseType", caseType, "summary");
        verifyNoMoreInteractions(entityService);
    }

    @Test
    public void getEntitiesForListName() {
        String listName = "L1";
        String simpleName1 = "nameOne";
        String data1 = "{ title: 'Title One' }";
        String simpleName2 = "nameTwo";
        String data2 = "{ title: 'Title Two' }";
        Entity entity1 = new Entity(1L, UUID.randomUUID(), simpleName1, data1, UUID.randomUUID(), true);
        Entity entity2 = new Entity(2L, UUID.randomUUID(), simpleName2, data2, UUID.randomUUID(), true);
        List<Entity> entitiesToReturn = List.of(entity1, entity2);

        when(entityService.getByEntityListName(listName)).thenReturn(entitiesToReturn);

        ResponseEntity<List<EntityDto>> result = entityResource.getEntitiesForListName(listName);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().size()).isEqualTo(2);
        assertThat(result.getBody().get(0).getSimpleName()).isEqualTo(simpleName1);
        assertThat(result.getBody().get(0).getData()).isEqualTo(data1);
        assertThat(result.getBody().get(1).getSimpleName()).isEqualTo(simpleName2);
        assertThat(result.getBody().get(1).getData()).isEqualTo(data2);

        verify(entityService).getByEntityListName(listName);
        verifyNoMoreInteractions(entityService);
    }

    @Test
    public void createEntity() {
        String listName = "L1";
        String simpleName = "name";
        String uuid = UUID.randomUUID().toString();
        String data = "data";
        EntityDto entityDto = new EntityDto(simpleName, uuid, data);

        ResponseEntity<String> response = entityResource.createEntity(listName, entityDto);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(entityService).createEntity(listName, entityDto);
        verifyNoMoreInteractions(entityService);
    }

}
