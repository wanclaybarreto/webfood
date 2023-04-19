function searchRest(categoriaId) {
	let searchType = document.querySelector("input[data-name='search-type']");
	
	if (categoriaId == null) {
		searchType.value = "Texto";
	} else {
		searchType.value = "Categoria";
		document.querySelector("input[data-name='categoria-id']").value = categoriaId;
	}
	
	document.getElementById("formSearch").submit();
}

function setCmd(cmd) {
	document.querySelector("input[name='cmd']").value = cmd;
	document.getElementById("formSearch").submit();
}

function filterItensCardapio(categoria) {
	if (categoria == null) {
		document.querySelector("#formFiltroItensCardapio input[name='categoria']").disabled = true;
	} else {
		document.querySelector("#formFiltroItensCardapio input[name='categoria']").value = categoria;
	}
	
	document.getElementById("formFiltroItensCardapio").submit();
}