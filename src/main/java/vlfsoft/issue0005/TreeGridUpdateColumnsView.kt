package vlfsoft.issue0005

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery
import com.vaadin.flow.data.provider.hierarchy.TreeData
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider
import com.vaadin.flow.function.SerializablePredicate
import com.vaadin.flow.router.Route

data class GridRow(val index: Int)

@VaadinDsl
fun <T : Any?> (@VaadinDsl HasComponents).treeGrid(dataProvider: HierarchicalDataProvider<T, SerializablePredicate<T>>? = null, block: (@VaadinDsl TreeGrid<T>).() -> Unit = {}) =
        init(TreeGrid<T>()) {
            if (dataProvider != null) this.dataProvider = dataProvider
            block()
        }

@Suppress("unused")
@Route("treegrid-update-columns-view")
class TreeGridUpdateColumnsView : VerticalLayout() {

    private val dataProvider = TreeDataProvider(TreeData<GridRow>().apply {
        val rootRow1 = GridRow(1)
        val rootRow2 = GridRow(2)

        addItems(null, rootRow1, rootRow2)

        addItems(rootRow1, GridRow(11), GridRow(12))

        addItems(rootRow2, GridRow(21), GridRow(22))

    })

    private val treeGrid: TreeGrid<GridRow>

    init {

        height = "100vh"

        label("issue05-treegrid-update-columns-view")

        treeGrid = addTreeGrid()

        treeGrid.updateGridColumns(4)
        treeGrid.dataProvider = dataProvider
        treeGrid.expandAll()

        button("1 column") {
            this.onLeftClick {
                treeGrid.updateGridColumns(1)
                treeGrid.dataProvider = dataProvider
                treeGrid.expandAll()
            }
        }

        button("2 columns") {
            this.onLeftClick {
                treeGrid.updateGridColumns(2)
                treeGrid.dataProvider = dataProvider
                treeGrid.expandAll()
            }
        }

        button("3 columns") {
            this.onLeftClick {
                treeGrid.updateGridColumns(3)
                treeGrid.dataProvider = dataProvider
                treeGrid.expandAll()
            }
        }

    }

    private fun addTreeGrid(): TreeGrid<GridRow> {
        return treeGrid {
            addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT)

            isHeightByRows = false

            addHierarchyColumn {
                it.index
            }

        }
    }

    private fun TreeGrid<GridRow>.updateGridColumns(columnsSize: Int) {

        fun @VaadinDsl Grid<GridRow>.addGridColumn(i: Int) {
            addComponentColumn<Component> { row ->
                val text = "row = GridRow(${row.index}, i = $i)"
                println(text)

                if (row.index in 1..10) Label().apply { this.text = text }
                else TextField()

            }
        }

        while (columns.size > 0) {
            removeColumn(columns[columns.size - 1])
        }

        addHierarchyColumn {
            it.index
        }

        for (i in 0 until columnsSize) {
            addGridColumn(i)
        }

        addComponentColumn { Icon(VaadinIcon.ABACUS) }
    }

}

private fun TreeGrid<GridRow>.expandAll() {
    expandRecursively((dataProvider as HierarchicalDataProvider<GridRow, SerializablePredicate<GridRow>>)
            .fetch(HierarchicalQuery(null, null)), Int.MAX_VALUE)
}



