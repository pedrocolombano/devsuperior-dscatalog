package com.devsuperior.dscatalog.tests;

import java.math.BigDecimal;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product("Iphone X",
				"Grave vídeos 4K, faça belos retratos e capture paisagens inteiras com o novo sistema de câmera dupla. Tire fotos incríveis com pouca luz usando o modo Noite. Veja cores fiéis em fotos, vídeos e jogos na tela Liquid Retina de 6,1 polegadas. Leve o desempenho sem precedentes do chip A13 Bionic para seus games, realidade aumentada e fotografia. Faça muito e recarregue pouco com a bateria para o dia todo.",
				new BigDecimal("4200.00"),
				"https://a-static.mlcdn.com.br/1500x1500/iphone-11-apple-64gb-preto-61-12mp-ios/magazineluiza/155610500/2815c001fcdff11766fcb266dca62daf.jpg");
		product.addCategory(new Category(2L, "Electronics"));
		return product;
	}

	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
	
}
