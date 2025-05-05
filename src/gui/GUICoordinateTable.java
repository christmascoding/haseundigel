package src.gui;

import com.sun.prism.image.Coords;

import java.util.ArrayList;
import java.util.List;

public class GUICoordinateTable {
    List<Coordinate> coordinates = new ArrayList<>();
    public GUICoordinateTable() {
        coordinates.add(new Coordinate(1350,1131));
        coordinates.add(new Coordinate(1251, 1281));
        coordinates.add(new Coordinate(1071, 1278));
        coordinates.add(new Coordinate(984, 1458));
        coordinates.add(new Coordinate(984, 1620));
        coordinates.add(new Coordinate(1170, 1722));
        coordinates.add(new Coordinate(1164, 1890));
        coordinates.add(new Coordinate(996, 1890));
        coordinates.add(new Coordinate(813, 1974));
        coordinates.add(new Coordinate(630, 1974));
        coordinates.add(new Coordinate(469, 1967)); //2er feld ist das links unten
        coordinates.add(new Coordinate(300, 1878));
        coordinates.add(new Coordinate(190, 1713));
        coordinates.add(new Coordinate(190, 1545));
        coordinates.add(new Coordinate(190, 1368));
        coordinates.add(new Coordinate(378, 1368));
        coordinates.add(new Coordinate(546, 1368));
        coordinates.add(new Coordinate(546, 1184));
        coordinates.add(new Coordinate(547, 1005));
        coordinates.add(new Coordinate(385, 1005));
        coordinates.add(new Coordinate(216, 932));
        coordinates.add(new Coordinate(216, 750));
        coordinates.add(new Coordinate(216, 578));
        coordinates.add(new Coordinate(306, 396));
        coordinates.add(new Coordinate(396, 228));
        coordinates.add(new Coordinate(564, 225));
        coordinates.add(new Coordinate(723, 225)); //karotte oben links
        coordinates.add(new Coordinate(817, 396));
        coordinates.add(new Coordinate(994, 479));
        coordinates.add(new Coordinate(1161, 477));
        coordinates.add(new Coordinate(1168, 318));
        coordinates.add(new Coordinate(1344, 308));
        coordinates.add(new Coordinate(1548, 308));
        coordinates.add(new Coordinate(1724, 318));
        coordinates.add(new Coordinate(1731, 477));
        coordinates.add(new Coordinate(1898, 479));
        coordinates.add(new Coordinate(2075, 396));
        coordinates.add(new Coordinate(2169, 225));
        coordinates.add(new Coordinate(2328, 225));
        coordinates.add(new Coordinate(2496, 228));
        coordinates.add(new Coordinate(2586, 396));
        coordinates.add(new Coordinate(2676, 578));
        coordinates.add(new Coordinate(2676, 750));
        coordinates.add(new Coordinate(2676, 932));
        coordinates.add(new Coordinate(2507, 1005));
        coordinates.add(new Coordinate(2345, 1005));
        coordinates.add(new Coordinate(2346, 1184));
        coordinates.add(new Coordinate(2346, 1368));
        coordinates.add(new Coordinate(2514, 1368));
        coordinates.add(new Coordinate(2682, 1368));
        coordinates.add(new Coordinate(2682, 1545));
        coordinates.add(new Coordinate(2682, 1713));
        coordinates.add(new Coordinate(2592, 1878));
        coordinates.add(new Coordinate(2423, 1967));
        coordinates.add(new Coordinate(2342, 1794));
        coordinates.add(new Coordinate(2253, 1623));
        coordinates.add(new Coordinate(2082, 1623));
        coordinates.add(new Coordinate(1992, 1790));
        coordinates.add(new Coordinate(1992, 1970));
        coordinates.add(new Coordinate(1821, 1970));
        coordinates.add(new Coordinate(1634, 1883));
        coordinates.add(new Coordinate(1634, 1722));
        coordinates.add(new Coordinate(1634, 1535));
        coordinates.add(new Coordinate(1634, 1358));
        coordinates.add(new Coordinate(1634, 1150));
    }

    /**
     * Returns coordinate for a field starting at index 0.
     * @return X/Y Coordinate of field (in relative to original image with a 2892x2184
     * @param field ID of field (starting at 0)
     */
    public Coordinate getCoordinateForField(int field){
        return coordinates.get(field);
    }

    /**
     * Returns the entire coordinate association table
     * @return List of coordinates.
     */
    public List<Coordinate> getCoordinateTable() {
        return coordinates;
    }
}
