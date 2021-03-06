package eu.aspire_fp7.adss.light.akb.ui.editors;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

/**
 * The AKB editor contributor.
 */
public class AKBEditorContributor extends MultiPageEditorActionBarContributor
{
	/**
	 * Creates the contributor.
	 */
	public AKBEditorContributor()
	{
		super();
	}

	/**
	 * Contributes to the menu.
	 * @param manager
	 *            The menu manager.
	 **/
	@Override
	public void contributeToMenu(IMenuManager manager)
	{
	}

	/**
	 * Contributes to the tool-bar.
	 * @param manager
	 *            The tool-bar manager.
	 **/
	@Override
	public void contributeToToolBar(IToolBarManager manager)
	{
	}

	/**
	 * Sets the active page.
	 * @param part
	 *            The part to set as active.
	 **/
	@Override
	public void setActivePage(IEditorPart part)
	{
		if (activeEditorPart == part)
			return;

		activeEditorPart = part;

	}

	/** The active editor part. **/
	private IEditorPart activeEditorPart;
}
