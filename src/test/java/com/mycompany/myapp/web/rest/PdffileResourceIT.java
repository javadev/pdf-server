package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.PdfserverApp;
import com.mycompany.myapp.domain.Pdffile;
import com.mycompany.myapp.repository.PdffileRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PdffileResource} REST controller.
 */
@SpringBootTest(classes = PdfserverApp.class)
public class PdffileResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DATA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DATA_CONTENT_TYPE = "image/png";

    @Autowired
    private PdffileRepository pdffileRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPdffileMockMvc;

    private Pdffile pdffile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PdffileResource pdffileResource = new PdffileResource(pdffileRepository);
        this.restPdffileMockMvc = MockMvcBuilders.standaloneSetup(pdffileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pdffile createEntity(EntityManager em) {
        Pdffile pdffile = new Pdffile()
            .name(DEFAULT_NAME)
            .data(DEFAULT_DATA)
            .dataContentType(DEFAULT_DATA_CONTENT_TYPE);
        return pdffile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pdffile createUpdatedEntity(EntityManager em) {
        Pdffile pdffile = new Pdffile()
            .name(UPDATED_NAME)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE);
        return pdffile;
    }

    @BeforeEach
    public void initTest() {
        pdffile = createEntity(em);
    }

    @Test
    @Transactional
    public void createPdffile() throws Exception {
        int databaseSizeBeforeCreate = pdffileRepository.findAll().size();

        // Create the Pdffile
        restPdffileMockMvc.perform(post("/api/pdffiles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pdffile)))
            .andExpect(status().isCreated());

        // Validate the Pdffile in the database
        List<Pdffile> pdffileList = pdffileRepository.findAll();
        assertThat(pdffileList).hasSize(databaseSizeBeforeCreate + 1);
        Pdffile testPdffile = pdffileList.get(pdffileList.size() - 1);
        assertThat(testPdffile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPdffile.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testPdffile.getDataContentType()).isEqualTo(DEFAULT_DATA_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createPdffileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pdffileRepository.findAll().size();

        // Create the Pdffile with an existing ID
        pdffile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPdffileMockMvc.perform(post("/api/pdffiles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pdffile)))
            .andExpect(status().isBadRequest());

        // Validate the Pdffile in the database
        List<Pdffile> pdffileList = pdffileRepository.findAll();
        assertThat(pdffileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pdffileRepository.findAll().size();
        // set the field null
        pdffile.setName(null);

        // Create the Pdffile, which fails.

        restPdffileMockMvc.perform(post("/api/pdffiles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pdffile)))
            .andExpect(status().isBadRequest());

        List<Pdffile> pdffileList = pdffileRepository.findAll();
        assertThat(pdffileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPdffiles() throws Exception {
        // Initialize the database
        pdffileRepository.saveAndFlush(pdffile);

        // Get all the pdffileList
        restPdffileMockMvc.perform(get("/api/pdffiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pdffile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dataContentType").value(hasItem(DEFAULT_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA))));
    }
    
    @Test
    @Transactional
    public void getPdffile() throws Exception {
        // Initialize the database
        pdffileRepository.saveAndFlush(pdffile);

        // Get the pdffile
        restPdffileMockMvc.perform(get("/api/pdffiles/{id}", pdffile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pdffile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dataContentType").value(DEFAULT_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.data").value(Base64Utils.encodeToString(DEFAULT_DATA)));
    }

    @Test
    @Transactional
    public void getNonExistingPdffile() throws Exception {
        // Get the pdffile
        restPdffileMockMvc.perform(get("/api/pdffiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePdffile() throws Exception {
        // Initialize the database
        pdffileRepository.saveAndFlush(pdffile);

        int databaseSizeBeforeUpdate = pdffileRepository.findAll().size();

        // Update the pdffile
        Pdffile updatedPdffile = pdffileRepository.findById(pdffile.getId()).get();
        // Disconnect from session so that the updates on updatedPdffile are not directly saved in db
        em.detach(updatedPdffile);
        updatedPdffile
            .name(UPDATED_NAME)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE);

        restPdffileMockMvc.perform(put("/api/pdffiles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPdffile)))
            .andExpect(status().isOk());

        // Validate the Pdffile in the database
        List<Pdffile> pdffileList = pdffileRepository.findAll();
        assertThat(pdffileList).hasSize(databaseSizeBeforeUpdate);
        Pdffile testPdffile = pdffileList.get(pdffileList.size() - 1);
        assertThat(testPdffile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPdffile.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testPdffile.getDataContentType()).isEqualTo(UPDATED_DATA_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPdffile() throws Exception {
        int databaseSizeBeforeUpdate = pdffileRepository.findAll().size();

        // Create the Pdffile

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPdffileMockMvc.perform(put("/api/pdffiles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pdffile)))
            .andExpect(status().isBadRequest());

        // Validate the Pdffile in the database
        List<Pdffile> pdffileList = pdffileRepository.findAll();
        assertThat(pdffileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePdffile() throws Exception {
        // Initialize the database
        pdffileRepository.saveAndFlush(pdffile);

        int databaseSizeBeforeDelete = pdffileRepository.findAll().size();

        // Delete the pdffile
        restPdffileMockMvc.perform(delete("/api/pdffiles/{id}", pdffile.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pdffile> pdffileList = pdffileRepository.findAll();
        assertThat(pdffileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
