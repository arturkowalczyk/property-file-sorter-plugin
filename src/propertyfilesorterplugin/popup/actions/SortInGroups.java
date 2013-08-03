package propertyfilesorterplugin.popup.actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.eclipse.core.internal.resources.File;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.PluginAction;

import pl.com.bits.propertyfile.sorter.domain.PropertyFile;
import pl.com.bits.propertyfile.sorter.domain.PropertyFileParser;

@SuppressWarnings("restriction")
public class SortInGroups implements IObjectActionDelegate {

  private static final String PLUGIN_NAME = "Property file sorter";
  private static final String SUCCESS_MESSAGE = "File '%s' has been sorted.";

  private Shell shell;

  /**
   * Constructor for Action1.
   */
  public SortInGroups() {
    super();
  }

  /**
   * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
   */
  public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    shell = targetPart.getSite().getShell();
  }

  /**
   * @see IActionDelegate#run(IAction)
   */
  public void run(IAction action) {
    PluginAction pa = (PluginAction) action;
    IStructuredSelection selection = (IStructuredSelection) pa.getSelection();

    for (Object object : selection.toArray()) {
      if (object instanceof File) {
        File file = (File) object;

        try {
          PropertyFileParser parser = new PropertyFileParser(file.getContents(true));
          PropertyFile propertyFile = parser.parse();

          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          propertyFile.serialize(baos);
          baos.close();

          InputStream inStream = new ByteArrayInputStream(baos.toByteArray());
          file.setContents(inStream, true, true, null);

          MessageDialog.openInformation(shell, PLUGIN_NAME, String.format(SUCCESS_MESSAGE, file.getName()));
        } catch (Exception e) {
          MessageDialog.openError(shell, PLUGIN_NAME, e.getMessage());
        }

      }
    }

  }

  /**
   * @see IActionDelegate#selectionChanged(IAction, ISelection)
   */
  public void selectionChanged(IAction action, ISelection selection) {
  }

}
