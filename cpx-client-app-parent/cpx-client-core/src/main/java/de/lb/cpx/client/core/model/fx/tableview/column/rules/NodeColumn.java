/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.tableview.column.rules;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 *
 * @author gerschmann
 * @param <T> table item
 */
public abstract class NodeColumn<T> extends TableColumn<T, Node>{

    private static final Logger LOG = Logger.getLogger(NodeColumn.class.getName());
        private SimpleObjectProperty<Node> property;
    public NodeColumn(String pTitle){
        super(pTitle);
        setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, Node>, ObservableValue<Node>>() {
//            private final Map<T, ObjectProperty<Integer>> valueMap = new HashMap<>();

            @Override
            public ObservableValue<Node> call(TableColumn.CellDataFeatures<T, Node> param) {
                if (param.getValue() != null) {
                    try {
                        T val = param.getValue();
//                        ObjectProperty<Integer> property = valueMap.get(val);
//                        if (property == null) {
//                            property = getProperty(val);
//                            valueMap.put(val, property);
//                        }
                        property = new SimpleObjectProperty<>();
                        property.set(getDisplayNode4Value(val));
                        return property;
                    } catch (ClassCastException ex) {
                        LOG.log(Level.WARNING, "ItemValue is not expected Type!", ex);
                    }
                }
                return null;
            }
        });
    }
    
    protected Node getDisplayNode4Value(T pValue){// rulesdto
        return extractValue(pValue);
    }
    
    protected abstract Node extractValue(T pValue);
}
