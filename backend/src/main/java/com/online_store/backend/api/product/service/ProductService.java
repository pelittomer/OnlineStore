package com.online_store.backend.api.product.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.online_store.backend.api.brand.entity.Brand;
import com.online_store.backend.api.brand.repository.BrandRepository;
import com.online_store.backend.api.brand.utils.BrandUtilsService;
import com.online_store.backend.api.category.entity.Category;
import com.online_store.backend.api.category.repository.CategoryRepository;
import com.online_store.backend.api.category.utils.CategoryUtilsService;
import com.online_store.backend.api.company.entity.Company;
import com.online_store.backend.api.company.entity.CompanyStatus;
import com.online_store.backend.api.company.service.CompanyService;
import com.online_store.backend.api.company.utils.CompanyUtilsService;
import com.online_store.backend.api.product.dto.base.FeatureDto;
import com.online_store.backend.api.product.dto.request.ProductCriteriaRequestDto;
import com.online_store.backend.api.product.dto.request.ProductRequestDto;
import com.online_store.backend.api.product.dto.request.ProductStockRequestDto;
import com.online_store.backend.api.product.dto.response.CriteriaOptionsResponseDto;
import com.online_store.backend.api.product.dto.response.ProductCriteriaResponseDto;
import com.online_store.backend.api.product.dto.response.ProductDetailResponseDto;
import com.online_store.backend.api.product.dto.response.ProductResponseDto;
import com.online_store.backend.api.product.dto.response.ProductStockResponseDto;
import com.online_store.backend.api.product.dto.response.StockVariationResponseDto;
import com.online_store.backend.api.product.entity.CriteriaOptions;
import com.online_store.backend.api.product.entity.Product;
import com.online_store.backend.api.product.entity.ProductCriteria;
import com.online_store.backend.api.product.entity.ProductDetail;
import com.online_store.backend.api.product.entity.ProductStock;
import com.online_store.backend.api.product.entity.embeddables.Feature;
import com.online_store.backend.api.product.entity.embeddables.StockVariation;
import com.online_store.backend.api.product.repository.ProductRepository;
import com.online_store.backend.api.shipper.entity.Shipper;
import com.online_store.backend.api.shipper.repository.ShipperRepository;
import com.online_store.backend.api.shipper.utils.ShipperUtilsService;
import com.online_store.backend.api.upload.entity.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.api.variation.dto.response.VariationOptionResponseDto;
import com.online_store.backend.api.variation.dto.response.VariationResponseDto;
import com.online_store.backend.api.variation.entity.Variation;
import com.online_store.backend.api.variation.entity.VariationOption;
import com.online_store.backend.api.variation.repository.VariationOptionRepository;
import com.online_store.backend.api.variation.repository.VariationRepository;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
        private final ProductRepository productRepository;
        private final VariationOptionRepository variationOptionRepository;
        private final CategoryRepository categoryRepository;
        private final VariationRepository variationRepository;
        private final BrandRepository brandRepository;
        private final ShipperRepository shipperRepository;
        private final CommonUtilsService commonUtilsService;
        private final CompanyService companyService;
        private final UploadService uploadService;
        private final ShipperUtilsService shipperUtilsService;
        private final BrandUtilsService brandUtilsService;
        private final CompanyUtilsService companyUtilsService;
        private final CategoryUtilsService categoryUtilsService;

        @Transactional
        public void createProduct(ProductRequestDto productRequestDto, MultipartHttpServletRequest request) {
                Company currentCompany = companyService.getCurrentUserCompanyForEdit();
                if (currentCompany.getStatus() != CompanyStatus.Approved) {
                        throw new IllegalStateException("Company is not approved! Product creation is not allowed.");
                }

                Brand brand = findEntityById(brandRepository::findById, productRequestDto.getBrand(), "Brand");
                Shipper shipper = findEntityById(shipperRepository::findById, productRequestDto.getShipper(),
                                "Shipper");
                Category category = findEntityById(categoryRepository::findById, productRequestDto.getCategory(),
                                "Category");

                Map<String, MultipartFile> fileMap = request.getFileMap();
                Set<Upload> productImages = new HashSet<>();

                processAndAddImage(fileMap, "images", productImages);

                List<ProductCriteria> productCriterias = processProductCriterias(
                                productRequestDto.getProductDetail().getCriteria(), fileMap);

                fileMap.values().forEach(remainingFile -> {
                        commonUtilsService.checkImageFileType(remainingFile);
                        productImages.add(uploadService.createFile(remainingFile));
                });

                ProductDetail productDetail = ProductDetail.builder()
                                .description(productRequestDto.getProductDetail().getDescription())
                                .shortDescription(productRequestDto.getProductDetail().getShortDescription())
                                .features(productRequestDto.getProductDetail().getFeatures() != null
                                                ? productRequestDto.getProductDetail().getFeatures().stream()
                                                                .map(featureDto -> Feature.builder()
                                                                                .name(featureDto.getName())
                                                                                .value(featureDto.getValue())
                                                                                .build())
                                                                .collect(Collectors.toSet())
                                                : new HashSet<>())
                                .build();

                List<ProductStock> productStocks = productRequestDto.getProductStock().stream()
                                .map(this::mapToProductStock)
                                .collect(Collectors.toList());

                Product product = Product.builder()
                                .name(productRequestDto.getName())
                                .price(productRequestDto.getPrice())
                                .brand(brand)
                                .images(productImages)
                                .category(category)
                                .shipper(shipper)
                                .company(currentCompany)
                                .isPublished(productRequestDto.getIsPublished())
                                .productDetail(productDetail)
                                .productStocks(productStocks)
                                .build();

                productDetail.setProduct(product);

                productCriterias.forEach(criteria -> criteria.setProductDetail(productDetail));
                productDetail.setCriteria(productCriterias);

                productStocks.forEach(stock -> stock.setProduct(product));

                productRepository.save(product);
        }

        private <T> T findEntityById(Function<Long, java.util.Optional<T>> finder, Long id, String entityName) {
                return finder.apply(id)
                                .orElseThrow(() -> new IllegalArgumentException(entityName + " not found!"));
        }

        private void processAndAddImage(Map<String, MultipartFile> fileMap, String key, Set<Upload> images) {
                MultipartFile file = fileMap.get(key);
                if (file != null && !file.isEmpty()) {
                        commonUtilsService.checkImageFileType(file);
                        images.add(uploadService.createFile(file));
                        fileMap.remove(key);
                }
        }

        private List<ProductCriteria> processProductCriterias(List<ProductCriteriaRequestDto> criteriaRequestDtos,
                        Map<String, MultipartFile> fileMap) {
                if (criteriaRequestDtos == null || criteriaRequestDtos.isEmpty()) {
                        return List.of();
                }

                return criteriaRequestDtos.stream()
                                .map(criteriaDto -> {
                                        Variation variation = findEntityById(variationRepository::findById,
                                                        criteriaDto.getVariation(), "Variation");
                                        Set<CriteriaOptions> options = criteriaDto.getOptions().stream()
                                                        .map(optionDto -> {
                                                                VariationOption variationOption = findEntityById(
                                                                                variationOptionRepository::findById,
                                                                                optionDto.getOption().getId(),
                                                                                "Option");
                                                                Set<Upload> optionImages = new HashSet<>();

                                                                if (optionDto.getOption() != null) {
                                                                        processAndAddImage(fileMap,
                                                                                        optionDto.getOption()
                                                                                                        .toString(),
                                                                                        optionImages);
                                                                }
                                                                return CriteriaOptions.builder()
                                                                                .option(variationOption)
                                                                                .images(optionImages)
                                                                                .build();
                                                        })
                                                        .collect(Collectors.toSet());

                                        ProductCriteria productCriteria = ProductCriteria.builder()
                                                        .variation(variation)
                                                        .options(options)
                                                        .build();

                                        options.forEach(option -> option.setProductCriteria(productCriteria));

                                        return productCriteria;
                                })
                                .collect(Collectors.toList());
        }

        private ProductStock mapToProductStock(ProductStockRequestDto productStockRequestDto) {
                Set<StockVariation> stockVariations = productStockRequestDto.getStockVariations().stream()
                                .map(variation -> StockVariation.builder()
                                                .variation(findEntityById(variationRepository::findById,
                                                                variation.getVariation(), "Variation for stock"))
                                                .variationOption(findEntityById(variationOptionRepository::findById,
                                                                variation.getVariationOption(),
                                                                "Variation Option for stock"))
                                                .build())
                                .collect(Collectors.toSet());

                return ProductStock.builder()
                                .additionalPrice(productStockRequestDto.getAdditionalPrica())
                                .stockQuantity(productStockRequestDto.getStockQuantity())
                                .isLimited(productStockRequestDto.getIsLimited())
                                .replenishQuantity(productStockRequestDto.getReplenishQuantity())
                                .stockVariations(stockVariations)
                                .build();
        }

        public List<ProductResponseDto> getProducts() {
                List<Product> products = productRepository.findAll();
                return products.stream()
                                .map(this::mapProductToProductResponseDto)
                                .toList();
        }

        private ProductResponseDto mapProductToProductResponseDto(Product product) {
                return ProductResponseDto.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .price(product.getPrice())
                                .discount(product.getDiscount())
                                .isPublished(product.getIsPublished())
                                .images(product.getImages() != null
                                                ? product.getImages().stream().map(Upload::getId).toList()
                                                : null)
                                .brand(product.getBrand() != null
                                                ? brandUtilsService.mapBrandorResponseDto(product.getBrand())
                                                : null)
                                .shipper(product.getShipper() != null
                                                ? shipperUtilsService.mapShipperTorResponseDto(product.getShipper())
                                                : null)
                                .company(product.getCompany() != null
                                                ? companyUtilsService.mapCompanyTorResponseDto(product.getCompany())
                                                : null)
                                .category(product.getCategory() != null
                                                ? categoryUtilsService.mapCategoryToResponseDto(product.getCategory())
                                                : null)
                                .productDetail(mapProductDetailToProductDetailResponseDto(product.getProductDetail()))
                                .productStock(product.getProductStocks() != null ? product.getProductStocks().stream()
                                                .map(this::mapProductStockToProductStockResponseDto)
                                                .toList() : null)
                                .createdAt(product.getCreatedAt())
                                .updatedAt(product.getUpdatedAt())
                                .build();
        }

        private ProductDetailResponseDto mapProductDetailToProductDetailResponseDto(ProductDetail productDetail) {
                if (productDetail == null) {
                        return null;
                }
                return ProductDetailResponseDto.builder()
                                .id(productDetail.getId())
                                .description(productDetail.getDescription())
                                .shortDescription(productDetail.getShortDescription())
                                .features(productDetail.getFeatures() != null ? productDetail.getFeatures().stream()
                                                .map(feature -> FeatureDto.builder().name(feature.getName())
                                                                .value(feature.getValue()).build())
                                                .collect(Collectors.toSet()) : null)
                                .criteria(productDetail.getCriteria() != null ? productDetail.getCriteria().stream()
                                                .map(this::mapProductCriteriaToProductCriteriaResponseDto)
                                                .toList() : null)
                                .build();
        }

        private ProductCriteriaResponseDto mapProductCriteriaToProductCriteriaResponseDto(ProductCriteria criteria) {
                return ProductCriteriaResponseDto.builder()
                                .id(criteria.getId())
                                .variation(criteria.getVariation() != null ? VariationResponseDto.builder()
                                                .id(criteria.getVariation().getId())
                                                .name(criteria.getVariation().getName())
                                                .options(criteria.getVariation().getVariationOptions() != null
                                                                ? criteria.getVariation().getVariationOptions().stream()
                                                                                .map(option -> VariationOptionResponseDto
                                                                                                .builder()
                                                                                                .id(option.getId())
                                                                                                .name(option.getName())
                                                                                                .build())
                                                                                .collect(Collectors.toSet())
                                                                : null)
                                                .build() : null)
                                .options(criteria.getOptions() != null ? criteria.getOptions().stream()
                                                .map(this::mapCriteriaOptionToCriteriaOptionsResponseDto)
                                                .collect(Collectors.toSet()) : null)
                                .build();
        }

        private CriteriaOptionsResponseDto mapCriteriaOptionToCriteriaOptionsResponseDto(CriteriaOptions option) {
                return CriteriaOptionsResponseDto.builder()
                                .id(option.getId())
                                .option(option.getOption() != null ? VariationOptionResponseDto.builder()
                                                .id(option.getOption().getId())
                                                .name(option.getOption().getName())
                                                .build() : null)
                                .images(option.getImages() != null ? option.getImages().stream()
                                                .map(Upload::getId)
                                                .collect(Collectors.toSet()) : null)
                                .build();
        }

        private ProductStockResponseDto mapProductStockToProductStockResponseDto(ProductStock stock) {
                return ProductStockResponseDto.builder()
                                .id(stock.getId())
                                .stockQuantity(stock.getStockQuantity())
                                .additionalPrice(stock.getAdditionalPrice())
                                .isLimited(stock.getIsLimited())
                                .replenishQuantity(stock.getReplenishQuantity())
                                .stockVariations(
                                                stock.getStockVariations() != null ? stock.getStockVariations().stream()
                                                                .map(this::mapStockVariationToStockVariationResponseDto)
                                                                .collect(Collectors.toSet()) : null)
                                .build();
        }

        private StockVariationResponseDto mapStockVariationToStockVariationResponseDto(StockVariation variation) {
                return StockVariationResponseDto.builder()
                                .variation(variation.getVariation() != null ? VariationResponseDto.builder()
                                                .id(variation.getVariation().getId())
                                                .name(variation.getVariation().getName())
                                                .build() : null)
                                .variationOption(
                                                variation.getVariationOption() != null
                                                                ? VariationOptionResponseDto.builder()
                                                                                .id(variation.getVariationOption()
                                                                                                .getId())
                                                                                .name(variation.getVariationOption()
                                                                                                .getName())
                                                                                .build()
                                                                : null)
                                .build();
        }
}
