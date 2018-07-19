package uk.gov.digital.ho.hocs.info.tenant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.entities.Tenant;
import uk.gov.digital.ho.hocs.info.repositories.TenantRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TenantServiceTest {

    @Mock
    private TenantRepository tenantRepository;

    public TenantService tenantService;

    @Before
    public void setUp() {
        this.tenantService = new TenantService(tenantRepository);
    }


    @Test
    public void shouldReturnOnlyTenantsFromRolesWhenRolesOnlyContainsTenants() {
        when(tenantRepository.findAll()).thenReturn(getReturnListOfTenants());

        List<String> tenants = tenantService.getTenantsFromRoles(
                new ArrayList<String>() {{
                    add("RSH");
                    add("DCU");
                    add("UKVI");
                }});

        assertThat(tenants.size()).isEqualTo(3);
        assertThat(tenants).contains("RSH");
        assertThat(tenants).contains("DCU");
        assertThat(tenants).contains("UKVI");
        assertThat(tenants).doesNotContain("FOI");
        assertThat(tenants).doesNotContain("HMPOCOR");
        assertThat(tenants).doesNotContain("HMPOCOL");


    }

    @Test
    public void shouldReturnOnlyTenantsFromRolesWhenRolesContainsTenantsAndNoneTenants() {
        when(tenantRepository.findAll()).thenReturn(getReturnListOfTenants());
        List<String> tenants = tenantService.getTenantsFromRoles(
                new ArrayList<String>() {{
                    add("Create");
                    add("Document");
                    add("RSH");
                    add("DCU");
                    add("UKVI");
                }});

        assertThat(tenants.size()).isEqualTo(3);
        assertThat(tenants).contains("RSH");
        assertThat(tenants).contains("DCU");
        assertThat(tenants).contains("UKVI");
        assertThat(tenants).doesNotContain("Create");
        assertThat(tenants).doesNotContain("Document");
        assertThat(tenants).doesNotContain("FOI");
        assertThat(tenants).doesNotContain("HMPOCOR");
        assertThat(tenants).doesNotContain("HMPOCOL");

    }

    @Test
    public void shouldReturnNoTenantsFromRolesWhenRolesOnlyContainsNoneTenants() {
        when(tenantRepository.findAll()).thenReturn(getReturnListOfTenants());
        List<String> tenants = tenantService.getTenantsFromRoles(
                new ArrayList<String>() {{
                    add("Create");
                    add("Document");

                }});

        assertThat(tenants.size()).isEqualTo(0);
        assertThat(tenants).doesNotContain("Create");
        assertThat(tenants).doesNotContain("Document");
        assertThat(tenants).doesNotContain("RSH");
        assertThat(tenants).doesNotContain("DCU");
        assertThat(tenants).doesNotContain("UKVI");
        assertThat(tenants).doesNotContain("FOI");
        assertThat(tenants).doesNotContain("HMPOCOR");
        assertThat(tenants).doesNotContain("HMPOCOL");

    }

    private List<Tenant> getReturnListOfTenants() {

        return new ArrayList<Tenant>() {{
            add(new Tenant(1, "RSH", new HashSet<>(), new ArrayList<>()));
            add(new Tenant(2, "DCU", new HashSet<>(), new ArrayList<>()));
            add(new Tenant(3, "UKVI", new HashSet<>(), new ArrayList<>()));
            add(new Tenant(4, "FOI", new HashSet<>(), new ArrayList<>()));
            add(new Tenant(5, "HMPOCOR", new HashSet<>(), new ArrayList<>()));
            add(new Tenant(6, "HMPOCOL", new HashSet<>(), new ArrayList<>()));
        }};
    }
}