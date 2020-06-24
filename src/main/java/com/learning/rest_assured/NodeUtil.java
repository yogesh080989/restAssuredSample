package com.flexnet.codeinsight.plugins.codeinsight_plugin;

import hudson.FilePath;
import hudson.model.Computer;
import hudson.model.Node;
import jenkins.model.Jenkins;

public class NodeUtil {
    private FilePath workSpace;

    public NodeUtil(FilePath workSpace){
        this.workSpace=workSpace;
    }
    public Node returnNode(){
        Node n=workspaceToNode(workSpace);
        return n;
    }

    private static Node workspaceToNode(FilePath workspace) {
        Jenkins j = Jenkins.getInstance();
        if (workspace.isRemote()) {
            for (Computer c : j.getComputers()) {
                if (c.getChannel() == workspace.getChannel()) {
                    Node n = c.getNode();
                    if (n != null) {
                        return n;
                    }
                }
            }
        }
        return j;
    }
}
