package net.sf.openrocket.preset.loader;

import java.util.HashMap;
import java.util.Map;

import net.sf.openrocket.material.Material;
import net.sf.openrocket.preset.TypedKey;
import net.sf.openrocket.preset.TypedPropertyMap;
import net.sf.openrocket.util.BugException;

public class MaterialLoader extends RocksimComponentFileLoader {

	private Map<String,Material> materialMap = new HashMap<String,Material>();
	
	private final static TypedKey<String> MATERIALNAME = new TypedKey<String>("MaterialName", String.class);
	private final static TypedKey<String> UNITS = new TypedKey<String>("Units", String.class);
	private final static TypedKey<Double> DENSITY = new TypedKey<Double>("Density", Double.class);
	
	public MaterialLoader() {
		super();
		fileColumns.add( new StringColumnParser("Material Name", MATERIALNAME) );
		fileColumns.add( new StringColumnParser("Units", UNITS));
		fileColumns.add( new DoubleColumnParser("Density", DENSITY));
	}

	@Override
	protected RocksimComponentFileType getFileType() {
		return RocksimComponentFileType.MATERIAL;
	}

	public Map<String, Material> getMaterialMap() {
		return materialMap;
	}

	@Override
	protected void postProcess(TypedPropertyMap props) {
		String name = props.get(MATERIALNAME);
		String unit = props.get(UNITS);
		double density = props.get(DENSITY);
		
		String cleanedMaterialName = stripAll(name, '"').trim();
		
		if ( "g/cm".equals( unit ) ) {
			materialMap.put( cleanedMaterialName, new Material.Line(cleanedMaterialName, 0.1d * density, true));
		} else if ( "g/cm2".equals(unit) ) {
			materialMap.put( cleanedMaterialName, new Material.Surface(cleanedMaterialName, 10.0d * density, true));
		} else if ( "g/cm3".equals(unit) ) {
			materialMap.put( cleanedMaterialName, new Material.Bulk(cleanedMaterialName, 1000.0d * density, true));
		} else if ( "kg/m3".equals(unit) ) {
			materialMap.put( cleanedMaterialName, new Material.Bulk(cleanedMaterialName, density, true));
		} else if ( "lb/ft3".equals(unit) ) {
			materialMap.put( cleanedMaterialName, new Material.Bulk(cleanedMaterialName, 16.0184634d * density, true));
		} else if ( "oz/in".equals(unit) ) {
			materialMap.put( cleanedMaterialName, new Material.Line(cleanedMaterialName, 1.11612296d * density, true));
		} else if ( "oz/in2".equals(unit ) ) {
			materialMap.put( cleanedMaterialName, new Material.Surface(cleanedMaterialName, 43.94184876d * density, true));
		} else {
			throw new BugException("Unknown unit in Materials file: " + unit);
		}
	}

}