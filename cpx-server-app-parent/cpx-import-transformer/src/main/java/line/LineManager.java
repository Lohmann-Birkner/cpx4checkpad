/*
 * Copyright (c) 2017 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Dirk Niemeier
 */
public class LineManager {

    private static final Logger LOG = Logger.getLogger(LineManager.class.getName());
    public final Map<String, List<LineI>> map;

    public LineManager() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        final HashMap<String, List<LineI>> mapTmp = new HashMap<>();
        for (LineEntity lineEntity : AbstractLine.getLineEntities()) {
            mapTmp.put(lineEntity.clazzName, new ArrayList<>());
        }
        map = Collections.unmodifiableMap(mapTmp);
    }

    public boolean add(final LineI pLine) {
        if (pLine == null) {
            throw new IllegalArgumentException("line is null!");
        }
        return map.get(pLine.getClass().getName()).add(pLine);
    }

    /*
  public boolean remove(final LineI pLine) {
    if (pLine == null) {
      throw new CpxIllegalArgumentException("line is null!");
    }
    return map.get(pLine.getClass()).remove(pLine);
  }
     */
    public void clearAll() {
        Iterator<Map.Entry<String, List<LineI>>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().clear();
        }
    }

}
